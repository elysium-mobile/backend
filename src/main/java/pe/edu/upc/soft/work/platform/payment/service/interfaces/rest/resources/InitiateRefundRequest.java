package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * InitiateRefundRequest
 * DTO for initiating a refund
 */
public record InitiateRefundRequest(
        @NotNull
        @JsonProperty("orderId")
        Long orderId,

        @NotNull
        String reason,

        @NotNull
        @JsonProperty("refoundAmountCents")
        Integer refundAmountCents) {
}
