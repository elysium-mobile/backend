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
    /**
     *  Find dashboards by company ID.
     * @param companyId The ID of the company to find dashboards for.
     * @return  A list of dashboards associated with the specified company ID.
     */
    List<Dashboard> findByCompanyId(Long companyId);
}
