package pe.edu.upc.soft.work.platform.payment.service.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * PaymentRegisteredEvent
 * Event triggered when a new Payment is successfully registered.
 */
@Getter
public class PaymentRegisteredEvent extends ApplicationEvent {
    /** The ID of the registered payment. */
    private final Long paymentId;
    /** The ID of the order associated with the payment. */
    private final Long orderId;

    /**
     * PaymentRegisteredEvent Constructor
     * @param source    the source of the event
     * @param paymentId the ID of the registered payment
     * @param orderId   the ID of the associated order
     */
    public PaymentRegisteredEvent(Object source, Long paymentId, Long orderId) {
        super(source);
        this.paymentId = paymentId;
        this.orderId = orderId;
    }
}
