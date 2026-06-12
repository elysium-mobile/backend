package pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates.Company;

import java.awt.*;
import java.util.List;

/**
 * Repository interface for managing Company entities.
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    List<Company> findByNameContainingIgnoreCase(String name);

    boolean existsByRUC(String ruc);
}
