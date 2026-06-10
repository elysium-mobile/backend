package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * StripeCheckoutResponse
 * Response body for POST /api/v1/stripe/checkout.
 * Returns the clientSecret that Stripe.js uses to confirm the payment on the frontend.
 */
public record StripeCheckoutResponse(
        @JsonProperty("clientSecret")
        String clientSecret
) {}
