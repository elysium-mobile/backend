package pe.edu.upc.soft.work.platform.payment.service.domain.model.events;

import org.springframework.context.ApplicationEvent;

import java.util.Objects;

/**
 * Domain event published when a refund is successfully completed through Stripe webhook
 */
public class RefundCompletedEvent extends ApplicationEvent {

    private final String refundId;
    private final Long paymentId;
    private final Long orderId;
    private final Integer refundAmountCents;
    private final String status;

    public RefundCompletedEvent(Object source,
                                String refundId,
                                Long paymentId,
                                Long orderId,
                                Integer refundAmountCents,
                                String status) {
        super(source);
        this.refundId = Objects.requireNonNull(refundId);
        this.paymentId = Objects.requireNonNull(paymentId);
        this.orderId = Objects.requireNonNull(orderId);
        this.refundAmountCents = Objects.requireNonNull(refundAmountCents);
        this.status = Objects.requireNonNull(status);
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

    public String getStatus() {
        return status;
    }
}
