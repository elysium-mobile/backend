package pe.edu.upc.soft.work.platform.payment.service.domain.model.commands;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects.PaymentStatus;

import java.util.Objects;
import java.util.Date;

/**
 * Command to create a new Payment
 */
public record CreatePaymentCommand(Long orderId, String transactionId, Date paymentDate, PaymentStatus paymentStatus, String paymentMethod) {

    /**
     * Constructor with validation
     */
    public CreatePaymentCommand {
        Objects.requireNonNull(orderId, "[CreatePaymentCommand] orderId must not be null");
        Objects.requireNonNull(transactionId, "[CreatePaymentCommand] transactionId must not be null");
        Objects.requireNonNull(paymentDate, "[CreatePaymentCommand] paymentDate must not be null");
        Objects.requireNonNull(paymentStatus, "[CreatePaymentCommand] paymentStatus must not be null");

    }
}
