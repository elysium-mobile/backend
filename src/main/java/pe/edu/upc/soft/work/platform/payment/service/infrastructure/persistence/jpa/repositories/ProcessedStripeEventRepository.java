package pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.ProcessedStripeEvent;

/**
 * Repository interface for managing ProcessedStripeEvent entities.
 * Used to guarantee idempotent processing of Stripe webhook events.
 */
@Repository
public interface ProcessedStripeEventRepository extends JpaRepository<ProcessedStripeEvent, Long> {

    /**
     * Checks whether a Stripe event has already been processed.
     *
     * @param stripeEventId the unique Stripe event ID
     * @return true if an event with that ID was already recorded
     */
    boolean existsByStripeEventId(String stripeEventId);
}
