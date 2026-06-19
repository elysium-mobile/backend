package pe.edu.upc.soft.work.platform.payment.service.domain.model.commands;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects.PaymentStatus;

import java.util.Objects;
import java.util.Date;

/**
 * Command to update an existing Payment
 */
public record UpdatePaymentCommand(Long paymentId, Long orderId, String transactionId, Date paymentDate,
                                   PaymentStatus paymentStatus, String paymentMethod) {

    /**
     * Constructor with validation
     */
    public UpdatePaymentCommand {
        Objects.requireNonNull(paymentId, "[UpdatePaymentCommand] paymentId must not be null");
        Objects.requireNonNull(paymentStatus, "[UpdatePaymentCommand] paymentStatus must not be null");

    }
}
