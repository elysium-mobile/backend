package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

/**
 * Request object for creating a new Payment.
 */
public record CreatePaymentRequest(
        @NotNull
        @NotBlank
        Long orderId,
        @NotNull
        @NotBlank
        String transactionId,
        @NotNull
        @NotBlank
        Date paymentDate
) {}
