package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.controllers;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateStripeCheckoutCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.events.StripePaymentFailedEvent;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.events.StripePaymentSucceededEvent;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.StripeCheckoutCommandService;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.stripe.StripeProperties;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.CreateStripeCheckoutRequest;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.StripeCheckoutResponse;

/**
 * StripeController
 * Exposes two endpoints:
 *   POST /api/v1/stripe/checkout  — creates a Stripe PaymentIntent and returns the clientSecret.
 *   POST /api/v1/stripe/webhook   — receives Stripe webhook events and publishes domain events.
 */
@CrossOrigin(origins = "*", methods = {RequestMethod.POST})
@RestController
@RequestMapping(value = "/api/v1/stripe", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Stripe", description = "Endpoints for Stripe payment integration")
public class StripeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StripeController.class);

    private final StripeCheckoutCommandService stripeCheckoutCommandService;
    private final StripeProperties stripeProperties;
    private final ApplicationEventPublisher eventPublisher;

    public StripeController(StripeCheckoutCommandService stripeCheckoutCommandService,
                            StripeProperties stripeProperties,
                            ApplicationEventPublisher eventPublisher) {
        this.stripeCheckoutCommandService = stripeCheckoutCommandService;
        this.stripeProperties = stripeProperties;
        this.eventPublisher = eventPublisher;
    }


    @Operation(summary = "Create a Stripe PaymentIntent",
            description = "Creates a Stripe PaymentIntent for an Order and returns the clientSecret for the frontend")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PaymentIntent created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = StripeCheckoutResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request or Order not found", content = @Content)
    })
    @PostMapping("/checkout")
    public ResponseEntity<StripeCheckoutResponse> createCheckout(@RequestBody CreateStripeCheckoutRequest request) {
        var command = new CreateStripeCheckoutCommand(request.orderId(), request.currency());
        var clientSecret = stripeCheckoutCommandService.handle(command);
        return ResponseEntity.ok(new StripeCheckoutResponse(clientSecret));
    }

    @Operation(summary = "Handle Stripe webhooks",
            description = "Receives Stripe webhook events (payment_intent.succeeded / payment_intent.payment_failed)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Webhook processed successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid Stripe signature", content = @Content),
            @ApiResponse(responseCode = "422", description = "Unhandled event type", content = @Content)
    })
    @PostMapping(value = "/webhook", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String stripeSignature) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, stripeSignature, stripeProperties.getWebhookSecret());
        } catch (SignatureVerificationException e) {
            LOGGER.warn("[StripeController] Invalid Stripe signature: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid Stripe signature");
        }

        switch (event.getType()) {
            case "payment_intent.succeeded" -> {
                var paymentIntentData = (PaymentIntent) event.getDataObjectDeserializer()
                        .getObject()
                        .orElseThrow(() -> new RuntimeException("[StripeController] Could not deserialize PaymentIntent"));

                var orderIdStr = paymentIntentData.getMetadata().get("orderId");
                var orderId = Long.parseLong(orderIdStr);

                eventPublisher.publishEvent(new StripePaymentSucceededEvent(
                        this,
                        paymentIntentData.getId(),
                        orderId,
                        paymentIntentData.getAmountReceived()));

                LOGGER.info("[StripeController] payment_intent.succeeded published for Order ID: {}", orderId);
            }
            case "payment_intent.payment_failed" -> {
                var paymentIntentData = (PaymentIntent) event.getDataObjectDeserializer()
                        .getObject()
                        .orElseThrow(() -> new RuntimeException("[StripeController] Could not deserialize PaymentIntent"));

                var orderIdStr = paymentIntentData.getMetadata().get("orderId");
                var orderId = Long.parseLong(orderIdStr);
                var failureReason = paymentIntentData.getLastPaymentError() != null
                        ? paymentIntentData.getLastPaymentError().getMessage()
                        : "Unknown reason";

                eventPublisher.publishEvent(new StripePaymentFailedEvent(
                        this,
                        paymentIntentData.getId(),
                        orderId,
                        failureReason));

                LOGGER.warn("[StripeController] payment_intent.payment_failed published for Order ID: {}", orderId);
            }
            default -> {
                LOGGER.info("[StripeController] Unhandled Stripe event type: {}", event.getType());
                return ResponseEntity.unprocessableEntity().body("Unhandled event type: " + event.getType());
            }
        }

        return ResponseEntity.ok("Webhook processed");
    }
}
