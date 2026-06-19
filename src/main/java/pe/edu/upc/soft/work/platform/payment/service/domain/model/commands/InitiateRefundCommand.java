package pe.edu.upc.soft.work.platform.payment.service.domain.model.commands;

import java.util.Objects;

/**
 * Command to initiate a refund for an existing Payment
 */
public record InitiateRefundCommand(Long paymentId, Long orderId, String reason, Integer refundAmountCents) {

    /**
     * Constructor with validation
     */
    public InitiateRefundCommand {
        Objects.requireNonNull(paymentId, "[InitiateRefundCommand] paymentId must not be null");
        Objects.requireNonNull(orderId, "[InitiateRefundCommand] orderId must not be null");
    }
}
