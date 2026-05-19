package pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates.Membership;

/**
 * Repository interface for managing Membership entities.
 */
@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
}
