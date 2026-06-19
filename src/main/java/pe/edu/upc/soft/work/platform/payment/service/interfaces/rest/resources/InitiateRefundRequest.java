package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * InitiateRefundRequest
 * DTO for initiating a refund
 */
public record InitiateRefundRequest(
        @NotNull(message = "orderId is required")
        @Schema(description = "Order ID to refund", example = "100")
        Long orderId,

        @Schema(description = "Reason for refund", example = "Customer requested refund", nullable = true)
        String reason,

        @Positive(message = "refundAmountCents must be positive")
        @Schema(description = "Amount to refund in cents (null for full refund)", example = "5000", nullable = true)
        Integer refundAmountCents) {
}
