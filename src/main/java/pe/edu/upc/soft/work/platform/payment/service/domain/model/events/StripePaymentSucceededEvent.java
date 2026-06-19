package pe.edu.upc.soft.work.platform.payment.service.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * StripePaymentSucceededEvent
 * Domain event published when Stripe confirms that a PaymentIntent
 * has been fully paid (payment_intent.succeeded webhook).
 * The event carries just enough data for downstream handlers to
 * create a Payment record and activate the related Membership.
 */
@Getter
public class StripePaymentSucceededEvent extends ApplicationEvent {

    /** Stripe's PaymentIntent ID, used as the internal transaction ID. */
    private final String stripePaymentIntentId;

    /** Internal Order ID linked to the PaymentIntent metadata. */
    private final Long orderId;

    /** Amount charged in the currency's smallest unit (e.g. cents). */
    private final Long amountReceived;

    /**
     * StripePaymentSucceededEvent Constructor.
     * @param source                 the object that published the event
     * @param stripePaymentIntentId  Stripe's PaymentIntent ID
     * @param orderId                internal Order ID stored in Stripe metadata
     * @param amountReceived         amount confirmed by Stripe (in smallest currency unit)
     */
    public StripePaymentSucceededEvent(Object source,
                                       String stripePaymentIntentId,
                                       Long orderId,
                                       Long amountReceived) {
        super(source);
        this.stripePaymentIntentId = stripePaymentIntentId;
        this.orderId = orderId;
        this.amountReceived = amountReceived;
    }
}
