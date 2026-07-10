package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.controllers;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import pe.edu.upc.soft.work.platform.payment.service.application.internal.retries.PaymentRetryService;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateStripeCheckoutSessionCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.InitiateRefundCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.RetryPaymentCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.events.RefundCompletedEvent;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.events.StripePaymentFailedEvent;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.events.StripePaymentSucceededEvent;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.ProcessedStripeEvent;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.PaymentGatewayAdapter;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.RefundService;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.PaymentRepository;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.ProcessedStripeEventRepository;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.stripe.StripeProperties;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.*;

/**
 * Controller for managing Stripe payment operations.
 * Provides endpoints for creating Checkout Sessions, retries,
 * refunds, and Stripe webhook event handling.
 */
@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.GET})
@RestController
@RequestMapping(value = "/api/v1/payments/stripe", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Stripe Payments", description = "Stripe payment processing and management endpoints")
public class StripePaymentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StripePaymentController.class);

    private final PaymentGatewayAdapter paymentGatewayAdapter;
    private final RefundService refundService;
    private final PaymentRetryService paymentRetryService;
    private final StripeProperties stripeProperties;
    private final ApplicationEventPublisher eventPublisher;
    private final ProcessedStripeEventRepository processedStripeEventRepository;
    private final PaymentRepository paymentRepository;

    /**
     * Constructor for StripePaymentController.
     * @param paymentGatewayAdapter          Port for gateway operations (Checkout Session creation)
     * @param refundService                  Service for refund operations
     * @param paymentRetryService            Service for payment retries
     * @param stripeProperties               Configuration properties for Stripe
     * @param eventPublisher                 Publisher for domain events
     * @param processedStripeEventRepository Repository used to guarantee idempotent webhook processing
     */
    public StripePaymentController(PaymentGatewayAdapter paymentGatewayAdapter,
                                   RefundService refundService,
                                   PaymentRetryService paymentRetryService,
                                   StripeProperties stripeProperties,
                                   ApplicationEventPublisher eventPublisher,
                                   ProcessedStripeEventRepository processedStripeEventRepository,
                                   PaymentRepository paymentRepository) {
        this.paymentGatewayAdapter = paymentGatewayAdapter;
        this.refundService = refundService;
        this.paymentRetryService = paymentRetryService;
        this.stripeProperties = stripeProperties;
        this.eventPublisher = eventPublisher;
        this.processedStripeEventRepository = processedStripeEventRepository;
        this.paymentRepository = paymentRepository;
    }

    /**
     * Endpoint for creating a Stripe Checkout Session.
     * Returns the hosted checkout URL that the frontend should redirect to.
     *
     * @param request Request object containing order details and optional redirect URLs
     * @return ResponseEntity containing the checkout URL and session ID
     */
    @Operation(summary = "Create a Stripe Checkout Session",
        description = "Creates a new Stripe Checkout Session for an Order and returns the hosted checkout URL. "
            + "The frontend redirects the customer to this URL; Stripe handles the entire payment flow.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Checkout Session created successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = StripeCheckoutResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request or Order not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Stripe API error", content = @Content)
    })
    @PostMapping("/checkout")
    public ResponseEntity<StripeCheckoutResponse> createCheckout(
        @Valid @RequestBody CreateStripeCheckoutRequest request) {
        LOGGER.info("[StripePaymentController] Creating Checkout Session for Order ID: {}", request.orderId());

        var command = new CreateStripeCheckoutSessionCommand(
            request.orderId(),
            request.currency(),
            request.successUrl(),
            request.cancelUrl());

        var response = paymentGatewayAdapter.createCheckoutSession(command);

        return ResponseEntity.ok(new StripeCheckoutResponse(
            response.checkoutUrl(),
            response.sessionId()));
    }

    /**
     * Endpoint for retrying a failed payment.
     * Creates a new Checkout Session so the customer can attempt payment again.
     *
     * @param paymentId ID of the failed payment
     * @param request   Request object containing retry details
     * @return ResponseEntity containing the new checkout URL and transaction ID
     */
    @Operation(summary = "Retry a failed payment",
        description = "Creates a new Stripe Checkout Session to retry a previously failed payment attempt")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment retry initiated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request, Payment not found, or Payment is not in FAILED state"),
        @ApiResponse(responseCode = "500", description = "Stripe API error")
    })
    @PostMapping("/{paymentId}/retry")
    public ResponseEntity<PaymentRetryResponse> retryPayment(
        @Parameter(description = "Payment ID to retry") @PathVariable Long paymentId,
        @Valid @RequestBody RetryPaymentRequest request) {
        LOGGER.info("[StripePaymentController] Retrying payment ID: {} for Order: {}",
            paymentId, request.orderId());

        var command = new RetryPaymentCommand(paymentId, request.orderId(), request.currency());
        var response = paymentRetryService.retryPayment(command);

        return ResponseEntity.ok(new PaymentRetryResponse(
            response.paymentId(),
            response.checkoutUrl(),
            response.newTransactionId()));
    }

    /**
     * Endpoint for initiating a refund.
     * @param paymentId ID of the payment to refund
     * @param request   Request object containing refund details
     * @return ResponseEntity containing refund confirmation details
     */
    @Operation(summary = "Initiate a refund",
        description = "Creates a refund for a previously succeeded payment (full or partial)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Refund initiated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request or Payment not found"),
        @ApiResponse(responseCode = "422", description = "Cannot refund payment (wrong status)"),
        @ApiResponse(responseCode = "500", description = "Stripe API error")
    })
    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<RefundResponse> initiateRefund(
        @Parameter(description = "Payment ID to refund") @PathVariable Long paymentId,
        @Valid @RequestBody InitiateRefundRequest request) {
        LOGGER.info("[StripePaymentController] Initiating refund for Payment ID: {}", paymentId);

        var command = new InitiateRefundCommand(
            paymentId,
            request.orderId(),
            request.reason(),
            request.refundAmountCents());

        var response = refundService.initiateRefund(command);

        return ResponseEntity.ok(new RefundResponse(
            response.refundId(),
            response.paymentIntentId(),
            response.refundedAmountCents(),
            response.status()));
    }

    /**
     * Endpoint for processing Stripe webhook events.
     * Handles checkout.session.completed (primary for Checkout Session flow),
     * payment_intent.succeeded (fallback), payment_intent.payment_failed, and
     * charge.refunded.
     *
     * @param payload         Webhook event payload
     * @param stripeSignature Stripe signature header for verification
     * @return ResponseEntity indicating success or error status
     */
    @Operation(summary = "Handle Stripe webhooks",
        description = "Receives and processes Stripe webhook events "
            + "(checkout.session.completed, payment_intent.succeeded, "
            + "payment_intent.payment_failed, charge.refunded)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Webhook processed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid Stripe signature"),
        @ApiResponse(responseCode = "422", description = "Unhandled event type")
    })
    @PostMapping(value = "/webhook", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<String> handleWebhook(
        @RequestBody String payload,
        @RequestHeader("Stripe-Signature") String stripeSignature) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, stripeSignature, stripeProperties.getWebhookSecret());
        } catch (SignatureVerificationException e) {
            LOGGER.warn("[StripePaymentController] Invalid Stripe signature: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid Stripe signature");
        }

        // Idempotency guard: Stripe guarantees at-least-once delivery, so the same
        // event.id can arrive more than once (retries, duplicate deliveries). If we
        // already processed it, acknowledge with 200 without reprocessing, so Stripe
        // stops retrying and we don't create duplicate Payments/side effects.
        if (processedStripeEventRepository.existsByStripeEventId(event.getId())) {
            LOGGER.info("[StripePaymentController] Event {} already processed, skipping (idempotent no-op)", event.getId());
            return ResponseEntity.ok("Event already processed");
        }

        try {
            switch (event.getType()) {
                case "checkout.session.completed" -> handleCheckoutSessionCompleted(event);
                case "payment_intent.succeeded" -> handlePaymentSucceeded(event);
                case "payment_intent.payment_failed" -> handlePaymentFailed(event);
                case "charge.refunded" -> handleRefundCompleted(event);
                default -> {
                    LOGGER.info("[StripePaymentController] Unhandled event type: {}", event.getType());
                    return ResponseEntity.unprocessableEntity().body("Unhandled event type: " + event.getType());
                }
            }
            // Mark as processed only after the business logic above succeeded, and in
            // the same @Transactional boundary as the rest of the handling, so either
            // everything commits together or everything rolls back together.
            processedStripeEventRepository.save(new ProcessedStripeEvent(event.getId(), event.getType()));
            return ResponseEntity.ok("Webhook processed");
        } catch (Exception e) {
            LOGGER.error("[StripePaymentController] Error processing webhook: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error processing webhook: " + e.getMessage());
        }
    }

    /**
     * Handles checkout.session.completed webhook from Stripe — the PRIMARY
     * event for the Checkout Session flow.
     * <p>
     * Extracts the PaymentIntent ID, orderId, and (optionally) membershipId
     * from the Session metadata, then publishes StripePaymentSucceededEvent
     * so the downstream handler creates a Payment record and activates the
     * Membership.
     * <p>
     * Before publishing, validates that:
     * <ul>
     *   <li>The Session contains a non-null PaymentIntent ID</li>
     *   <li>The Session metadata contains a valid orderId</li>
     *   <li>No Payment record exists yet for this PaymentIntent (idempotency)</li>
     * </ul>
     */
    private void handleCheckoutSessionCompleted(Event event) {
        var session = (Session) event.getDataObjectDeserializer()
            .getObject()
            .orElseThrow(() -> new RuntimeException("Could not deserialize Session for checkout.session.completed"));

        var paymentIntentId = session.getPaymentIntent();
        if (paymentIntentId == null || paymentIntentId.isBlank()) {
            throw new RuntimeException("Session " + session.getId() + " completed but has no PaymentIntent. "
                + "This is unexpected for Mode.PAYMENT sessions.");
        }

        // Cross-check: was this PaymentIntent already processed via
        // payment_intent.succeeded arriving first?
        if (paymentRepository.existsByTransactionId(paymentIntentId)) {
            LOGGER.info("[StripePaymentController] PaymentIntent {} already processed via payment_intent.succeeded, skipping",
                paymentIntentId);
            return;
        }

        var metadata = session.getMetadata();
        var orderIdStr = metadata.get("orderId");
        if (orderIdStr == null || orderIdStr.isBlank()) {
            throw new RuntimeException("Session " + session.getId() + " metadata missing 'orderId'. "
                + "Ensure the Checkout Session is created with putMetadata(\"orderId\", ...).");
        }
        var orderId = Long.parseLong(orderIdStr);

        var amountTotal = session.getAmountTotal();
        if (amountTotal == null) {
            LOGGER.warn("[StripePaymentController] Session {} has null amountTotal, using 0", session.getId());
            amountTotal = 0L;
        }

        eventPublisher.publishEvent(new StripePaymentSucceededEvent(
            this,
            paymentIntentId,
            orderId,
            amountTotal));

        LOGGER.info("[StripePaymentController] checkout.session.completed processed for Session: {}, Order: {}, PaymentIntent: {}",
            session.getId(), orderId, paymentIntentId);
    }

    /**
     * Handles payment_intent.succeeded webhook.
     * With Checkout Session, the PaymentIntent metadata is set via
     * PaymentIntentData, so orderId is still resolvable.
     * <p>
     * This handler acts as a FALLBACK to checkout.session.completed.
     * Both events fire for the same payment. The downstream event handler
     * guards against duplicates via {@code existsByTransactionId()} and the
     * database unique constraint on {@code payments.transaction_id}.
     * To avoid unnecessary event publication, we skip outright if a Payment
     * with this transaction ID already exists.
     */
    private void handlePaymentSucceeded(Event event) {
        var paymentIntentData = (PaymentIntent) event.getDataObjectDeserializer()
            .getObject()
            .orElseThrow(() -> new RuntimeException("Could not deserialize PaymentIntent"));

        var paymentIntentId = paymentIntentData.getId();

        // Idempotency cross-check: if checkout.session.completed already
        // processed this PaymentIntent, skip without publishing an event.
        if (paymentRepository.existsByTransactionId(paymentIntentId)) {
            LOGGER.info("[StripePaymentController] PaymentIntent {} already processed via checkout.session.completed, skipping",
                paymentIntentId);
            return;
        }

        var orderIdStr = paymentIntentData.getMetadata().get("orderId");
        if (orderIdStr == null) throw new RuntimeException("Missing orderId in PaymentIntent metadata");
        var orderId = Long.parseLong(orderIdStr);

        eventPublisher.publishEvent(new StripePaymentSucceededEvent(
            this,
            paymentIntentId,
            orderId,
            paymentIntentData.getAmountReceived()));

        LOGGER.info("[StripePaymentController] payment_intent.succeeded published for Order ID: {}", orderId);
    }

    /**
     * Handles payment_intent.payment_failed webhook.
     */
    private void handlePaymentFailed(Event event) {
        var paymentIntentData = (PaymentIntent) event.getDataObjectDeserializer()
            .getObject()
            .orElseThrow(() -> new RuntimeException("Could not deserialize PaymentIntent"));

        var orderIdStr = paymentIntentData.getMetadata().get("orderId");
        if (orderIdStr == null) throw new RuntimeException("Missing orderId in PaymentIntent metadata");
        var orderId = Long.parseLong(orderIdStr);
        var failureReason = paymentIntentData.getLastPaymentError() != null
            ? paymentIntentData.getLastPaymentError().getMessage()
            : "Unknown reason";

        eventPublisher.publishEvent(new StripePaymentFailedEvent(
            this,
            paymentIntentData.getId(),
            orderId,
            failureReason));

        LOGGER.warn("[StripePaymentController] payment_intent.payment_failed published for Order ID: {}", orderId);
    }

    /**
     * Handles charge.refunded webhook.
     */
    private void handleRefundCompleted(Event event) {
        var refundData = (Refund) event.getDataObjectDeserializer()
            .getObject()
            .orElseThrow(() -> new RuntimeException("Could not deserialize Refund"));

        var orderIdStr = refundData.getMetadata().get("orderId");
        var paymentIdStr = refundData.getMetadata().get("paymentId");

        if (orderIdStr != null && paymentIdStr != null) {
            var orderId = Long.parseLong(orderIdStr);
            var paymentId = Long.parseLong(paymentIdStr);

            eventPublisher.publishEvent(new RefundCompletedEvent(
                this,
                refundData.getId(),
                paymentId,
                orderId,
                Math.toIntExact(refundData.getAmount()),
                refundData.getStatus()));

            LOGGER.info("[StripePaymentController] charge.refunded published for Refund ID: {}", refundData.getId());
        }
    }
}
