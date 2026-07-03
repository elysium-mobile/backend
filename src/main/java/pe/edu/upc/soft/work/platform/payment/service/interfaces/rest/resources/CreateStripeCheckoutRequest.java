package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotNull;

/**
 * CreateStripeCheckoutRequest
 * Request body for POST /api/v1/stripe/checkout.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateStripeCheckoutRequest(
        @NotNull
        Long orderId,
        String currency
) {}
