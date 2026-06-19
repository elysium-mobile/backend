package pe.edu.upc.soft.work.platform.payment.service.domain.model.commands;

import java.util.Objects;

/**
 * Command to retry a failed payment
 * Used when a customer wants to retry their payment after initial failure
 */
public record RetryPaymentCommand(Long paymentId, Long orderId, String currency) {

    /**
     * Constructor with validation
     */
    public RetryPaymentCommand {
        Objects.requireNonNull(paymentId, "[RetryPaymentCommand] paymentId must not be null");
        Objects.requireNonNull(orderId, "[RetryPaymentCommand] orderId must not be null");
        Objects.requireNonNull(currency, "[RetryPaymentCommand] currency must not be null");
    }
}
