package pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates.Payment;

/**
 * Repository interface for managing Payment entities.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

  /**
   * Checks whether a Payment with the given Stripe transaction/PaymentIntent ID already exists.
   * Used as a defense-in-depth idempotency guard when creating Payments from webhook events.
   *
   * @param transactionId the Stripe transaction (PaymentIntent) ID
   * @return true if a Payment with that transaction ID already exists
   */
  boolean existsByTransactionId(String transactionId);
}
