package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.controllers;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
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
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateStripeCheckoutCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.InitiateRefundCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.RetryPaymentCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.events.RefundCompletedEvent;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.events.StripePaymentFailedEvent;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.events.StripePaymentSucceededEvent;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.ProcessedStripeEvent;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.PaymentGatewayAdapter;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.RefundService;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.ProcessedStripeEventRepository;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.stripe.StripeProperties;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.*;

/**
 * Controller for managing Stripe payment operations.
 * Provides endpoints for payment creation, retries, refunds, and webhook event handling.
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

    /**
     * Constructor for StripePaymentController.
     * @param paymentGatewayAdapter          Service for gateway operations
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
                                   ProcessedStripeEventRepository processedStripeEventRepository) {
        this.paymentGatewayAdapter = paymentGatewayAdapter;
        this.refundService = refundService;
        this.paymentRetryService = paymentRetryService;
        this.stripeProperties = stripeProperties;
        this.eventPublisher = eventPublisher;
        this.processedStripeEventRepository = processedStripeEventRepository;
    }

    /**
     * Endpoint for creating a Stripe PaymentIntent.
     * @param request Request object containing order details
     * @return ResponseEntity containing the client secret for payment confirmation
     */
    @Operation(summary = "Create a Stripe PaymentIntent",
        description = "Creates a new Stripe PaymentIntent for an Order and returns the clientSecret for the frontend to confirm payment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "PaymentIntent created successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = StripeCheckoutResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request or Order not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Stripe API error", content = @Content)
    })
    @PostMapping("/checkout")
    public ResponseEntity<StripeCheckoutResponse> createCheckout(
        @Valid @RequestBody CreateStripeCheckoutRequest request) {
        LOGGER.info("[StripePaymentController] Creating checkout for Order ID: {}", request.orderId());

        var command = new CreateStripeCheckoutCommand(request.orderId(), request.currency());
        var response = paymentGatewayAdapter.createPaymentIntent(command);

        return ResponseEntity.ok(new StripeCheckoutResponse(response.clientSecret()));
    }

    /**
     * Endpoint for retrying a failed payment.
     * @param paymentId ID of the failed payment
     * @param request   Request object containing retry details
     * @return ResponseEntity containing new payment credentials
     */
    @Operation(summary = "Retry a failed payment",
        description = "Creates a new PaymentIntent to retry a previously failed payment attempt")
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
            response.clientSecret(),
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
     * @param payload         Webhook event payload
     * @param stripeSignature Stripe signature header for verification
     * @return ResponseEntity indicating success or error status
     */
    @Operation(summary = "Handle Stripe webhooks",
        description = "Receives and processes Stripe webhook events (payment_intent.succeeded, payment_intent.payment_failed, charge.refunded)")
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

    private void handlePaymentSucceeded(Event event) {
        var paymentIntentData = (PaymentIntent) event.getDataObjectDeserializer()
            .getObject()
            .orElseThrow(() -> new RuntimeException("Could not deserialize PaymentIntent"));

        var orderIdStr = paymentIntentData.getMetadata().get("orderId");
        if (orderIdStr == null) throw new RuntimeException("Missing orderId in PaymentIntent metadata");
        var orderId = Long.parseLong(orderIdStr);

        eventPublisher.publishEvent(new StripePaymentSucceededEvent(
            this,
            paymentIntentData.getId(),
            orderId,
            paymentIntentData.getAmountReceived()));

        LOGGER.info("[StripePaymentController] payment_intent.succeeded published for Order ID: {}", orderId);
    }

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
