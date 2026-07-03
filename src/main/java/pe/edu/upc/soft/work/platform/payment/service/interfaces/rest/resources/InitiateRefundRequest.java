package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * InitiateRefundRequest
 * DTO for initiating a refund
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record InitiateRefundRequest(
        @NotNull
        Long orderId,

        @NotNull
        String reason,

        @NotNull
        Integer refundAmountCents) {
}
