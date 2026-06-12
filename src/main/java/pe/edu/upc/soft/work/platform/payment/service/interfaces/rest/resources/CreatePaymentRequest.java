package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

/**
 * Request object for creating a new Payment.
 */
public record CreatePaymentRequest(
        @NotNull
        @NotBlank
        @JsonProperty("orderId")
        Long orderId,
        @NotNull
        @NotBlank
        @JsonProperty("transactionId")
        String transactionId,
        @NotNull
        @NotBlank
        @JsonProperty("paymentDate")
        Date paymentDate
) {}
