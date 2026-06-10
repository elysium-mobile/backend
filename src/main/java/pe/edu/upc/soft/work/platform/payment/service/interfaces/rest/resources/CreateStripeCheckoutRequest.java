package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

/**
 * CreateStripeCheckoutRequest
 * Request body for POST /api/v1/stripe/checkout.
 */
public record CreateStripeCheckoutRequest(
        @NotNull
        @JsonProperty("orderId")
        Long orderId,
        @JsonProperty("currency")
        String currency
) {}
