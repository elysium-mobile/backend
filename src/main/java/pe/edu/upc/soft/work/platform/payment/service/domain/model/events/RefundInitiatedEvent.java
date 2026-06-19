package pe.edu.upc.soft.work.platform.payment.service.domain.model.events;

import org.springframework.context.ApplicationEvent;

import java.util.Objects;

/**
 * Domain event published when a refund is initiated through Stripe API
 */
public class RefundInitiatedEvent extends ApplicationEvent {

    private final String refundId;
    private final Long paymentId;
    private final Long orderId;
    private final Integer refundAmountCents;
    private final String reason;

    public RefundInitiatedEvent(Object source, String refundId, Long paymentId, Long orderId, Integer refundAmountCents, String reason) {
        super(source);
        this.refundId = Objects.requireNonNull(refundId);
        this.paymentId = Objects.requireNonNull(paymentId);
        this.orderId = Objects.requireNonNull(orderId);
        this.refundAmountCents = refundAmountCents;
        this.reason = reason;
    }

    public String getRefundId() {
        return refundId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Integer getRefundAmountCents() {
        return refundAmountCents;
    }

    public String getReason() {
        return reason;
    }
}
