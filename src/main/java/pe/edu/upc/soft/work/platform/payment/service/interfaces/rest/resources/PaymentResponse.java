package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

import java.util.Date;

/**
 * Response object representing a Payment in the system.
 */
public record PaymentResponse(
        Long paymentId,
        Long orderId,
        String transactionId,
        Date paymentDate
) {}
