package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotNull;

/**
 * CreateStripeCheckoutRequest
 * Request body for POST /api/v1/payments/stripe/checkout.
 * Creates a Stripe Checkout Session for the specified Order.
 *
 * @param orderId    the internal Order ID to pay for
 * @param currency   optional three-letter ISO currency code (defaults to "usd")
 * @param successUrl optional URL to redirect after successful payment (overrides config)
 * @param cancelUrl  optional URL to redirect if payment is cancelled (overrides config)
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateStripeCheckoutRequest(
        @NotNull
        Long orderId,
        String currency,
        String successUrl,
        String cancelUrl
) {}
