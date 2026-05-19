package pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.MembershipPlan;

/**
 * Repository interface for managing MembershipPlan entities.
 */
@Repository
public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, Long> {
}
