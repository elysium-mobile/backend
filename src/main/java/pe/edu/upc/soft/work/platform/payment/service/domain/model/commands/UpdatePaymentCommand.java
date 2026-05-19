package pe.edu.upc.soft.work.platform.payment.service.domain.model.commands;

import java.util.Objects;
import java.util.Date;

/**
 * Command to update an existing Payment
 */
public record UpdatePaymentCommand(Long paymentId, Long orderId, String transactionId, Date paymentDate) {

    /**
     * Constructor with validation
     */
    public UpdatePaymentCommand {
        Objects.requireNonNull(paymentId, "[UpdatePaymentCommand] paymentId must not be null");
    }
}
