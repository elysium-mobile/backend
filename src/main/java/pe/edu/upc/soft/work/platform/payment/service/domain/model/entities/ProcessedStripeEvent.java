package pe.edu.upc.soft.work.platform.payment.service.domain.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

/**
 * ProcessedStripeEvent
 * Tracks Stripe webhook events (by their unique {@code event.id}) that have
 * already been processed, so retried/duplicate webhook deliveries can be
 * safely ignored (Stripe guarantees at-least-once delivery, not exactly-once).
 */
@Entity
@Table(name = "processed_stripe_events",
        uniqueConstraints = @UniqueConstraint(columnNames = "stripe_event_id"))
public class ProcessedStripeEvent extends AuditableAbstractAggregateRoot<ProcessedStripeEvent> {

    @Getter
    @Column(name = "stripe_event_id", nullable = false, unique = true, length = 255)
    private String stripeEventId;

    @Getter
    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    /**
     * Default constructor for JPA.
     */
    public ProcessedStripeEvent() {}

    /**
     * Creates a record marking a Stripe event as processed.
     *
     * @param stripeEventId the unique Stripe event ID (e.g. "evt_1N...")
     * @param eventType     the Stripe event type (e.g. "payment_intent.succeeded")
     */
    public ProcessedStripeEvent(String stripeEventId, String eventType) {
        this.stripeEventId = stripeEventId;
        this.eventType = eventType;
    }
}
