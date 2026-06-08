package pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates.Dashboard;

import java.util.List;

/**
 * Repository interface for managing Dashboard entities.
 */
@Repository
public interface DashboardRepository extends JpaRepository<Dashboard, Long> {
    List<Dashboard> findByCompanyId(Long companyId);
}
