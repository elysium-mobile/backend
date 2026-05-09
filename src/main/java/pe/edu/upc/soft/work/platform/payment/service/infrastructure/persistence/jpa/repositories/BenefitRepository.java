package pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.Benefit;

/**
 * Repository interface for managing Benefit entities.
 */
@Repository
public interface BenefitRepository extends JpaRepository<Benefit, Long> {
}
