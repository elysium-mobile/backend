package pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.UnitOfWork;

/**
 * Repository interface for managing UnitOfWork entities.
 */
@Repository
public interface UnitOfWorkRepository extends JpaRepository<UnitOfWork, Long> {
}
