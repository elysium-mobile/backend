package pe.edu.upc.soft.work.platform.payment.service.domain.model.events;

import org.springframework.context.ApplicationEvent;

import java.util.Objects;

/**
 * Domain event published when a payment is retried after initial failure
 */
public class PaymentRetryInitiatedEvent extends ApplicationEvent {

    private final Long paymentId;
    private final Long orderId;
    private final String newPaymentIntentId;
    private final String currency;

    public PaymentRetryInitiatedEvent(Object source,
                                      Long paymentId,
                                      Long orderId,
                                      String newPaymentIntentId,
                                      String currency) {
        super(source);
        this.paymentId = Objects.requireNonNull(paymentId);
        this.orderId = Objects.requireNonNull(orderId);
        this.newPaymentIntentId = Objects.requireNonNull(newPaymentIntentId);
        this.currency = Objects.requireNonNull(currency);
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getNewPaymentIntentId() {
        return newPaymentIntentId;
    }

    public String getCurrency() {
        return currency;
    }
}
