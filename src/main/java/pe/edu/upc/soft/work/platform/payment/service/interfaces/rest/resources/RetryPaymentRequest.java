package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * RetryPaymentRequest
 * DTO for retrying a failed payment
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RetryPaymentRequest(
        @NotNull
        Long orderId,

        @NotNull
        String currency) {
}

