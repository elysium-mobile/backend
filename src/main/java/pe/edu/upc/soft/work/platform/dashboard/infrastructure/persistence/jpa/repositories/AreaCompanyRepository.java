package pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.AreaCompany;

import java.util.List;

/**
 * Repository interface for managing AreaCompany entities.
 */
@Repository
public interface AreaCompanyRepository extends JpaRepository<AreaCompany, Long> {

  /**
   * Find all areas belonging to a specific company.
   * @param companyId the ID of the company
   * @return list of AreaCompany entities for that company
   */
  List<AreaCompany> findByCompanyId(Long companyId);
}
