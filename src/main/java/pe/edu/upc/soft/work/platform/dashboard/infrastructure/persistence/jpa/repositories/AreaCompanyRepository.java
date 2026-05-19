package pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.AreaCompany;

/**
 * Repository interface for managing AreaCompany entities.
 */
@Repository
public interface AreaCompanyRepository extends JpaRepository<AreaCompany, Long> {
}
