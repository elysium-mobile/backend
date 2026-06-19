package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * RetryPaymentRequest
 * DTO for retrying a failed payment
 */
public record RetryPaymentRequest(
        @NotNull(message = "orderId is required")
        @Schema(description = "Order ID to retry payment for", example = "100")
        Long orderId,

        @Schema(description = "Currency code (ISO 4217)", example = "USD", defaultValue = "USD")
        String currency) {
}

