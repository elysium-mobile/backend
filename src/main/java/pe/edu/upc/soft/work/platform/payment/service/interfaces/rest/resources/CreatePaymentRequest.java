package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects.PaymentStatus;

import java.util.Date;

/**
 * Request object for creating a new Payment.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreatePaymentRequest(
        @NotNull
        @NotBlank
        Long orderId,
        @NotNull
        @NotBlank
        String transactionId,
        @NotNull
        @NotBlank
        Date paymentDate,

        @NotNull
        @NotBlank
        String paymentStatus,

        @NotNull
        @NotBlank
        String paymentMethod
) {}
