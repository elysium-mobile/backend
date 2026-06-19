package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * RetryPaymentRequest
 * DTO for retrying a failed payment
 */
public record RetryPaymentRequest(
        @NotNull
        @JsonProperty("orderId")
        Long orderId,

        @NotNull
        @JsonProperty("currency")
        String currency) {
}

