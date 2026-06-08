package pe.edu.upc.soft.work.platform.profile.performance.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.aggregates.Performance;

import java.util.Optional;

/**
 * Repository interface for managing Performance entities.
 */
@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    Optional<Performance> findByEmployeeProfileId(Long employeeId);
}
