package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;


/**
 * StripeCheckoutResponse
 * Response body for POST /api/v1/stripe/checkout.
 * Returns the clientSecret that Stripe.js uses to confirm the payment on the frontend.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record StripeCheckoutResponse(
        String clientSecret
) {}
