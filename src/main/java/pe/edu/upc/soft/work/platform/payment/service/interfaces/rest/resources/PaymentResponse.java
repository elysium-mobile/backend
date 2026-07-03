package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import java.util.Date;

/**
 * Response object representing a Payment in the system.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PaymentResponse(
        Long paymentId,
        Long orderId,
        String transactionId,
        String paymentStatus,
        Date paymentDate,
        String paymentMethod
) {}
