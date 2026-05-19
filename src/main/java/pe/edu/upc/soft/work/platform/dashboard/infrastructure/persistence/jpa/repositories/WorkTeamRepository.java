package pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.WorkTeam;

/**
 * Repository interface for managing WorkTeam entities.
 */
@Repository
public interface WorkTeamRepository extends JpaRepository<WorkTeam, Long> {
}
