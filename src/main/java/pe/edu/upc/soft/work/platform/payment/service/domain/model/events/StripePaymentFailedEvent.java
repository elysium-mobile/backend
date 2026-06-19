package pe.edu.upc.soft.work.platform.payment.service.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * StripePaymentFailedEvent
 * Domain event published when Stripe reports that a PaymentIntent
 * has failed (payment_intent.payment_failed webhook).
 * Downstream handlers use this to mark the Membership as FAILED.
 */
@Getter
public class StripePaymentFailedEvent extends ApplicationEvent {

    /** Stripe's PaymentIntent ID. */
    private final String stripePaymentIntentId;

    /** Internal Order ID stored in Stripe metadata. */
    private final Long orderId;

    /** Human-readable failure reason provided by Stripe. */
    private final String failureReason;

    /**
     * StripePaymentFailedEvent Constructor.
     * @param source                the object that published the event
     * @param stripePaymentIntentId Stripe's PaymentIntent ID
     * @param orderId               internal Order ID stored in Stripe metadata
     * @param failureReason         reason for the payment failure from Stripe
     */
    public StripePaymentFailedEvent(Object source,
                                    String stripePaymentIntentId,
                                    Long orderId,
                                    String failureReason) {
        super(source);
        this.stripePaymentIntentId = stripePaymentIntentId;
        this.orderId = orderId;
        this.failureReason = failureReason;
    }
}
