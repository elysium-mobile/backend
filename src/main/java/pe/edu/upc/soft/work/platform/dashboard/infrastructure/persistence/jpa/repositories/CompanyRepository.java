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

    /**
     *  Find companies by name containing the specified string, ignoring case.
     * @param name  The string to search for in company names.
     * @return  A list of companies whose names contain the specified string, ignoring case.
     */
    List<Company> findByNameContainingIgnoreCase(String name);

    /**
     *  Check if a company with the specified RUC exists.
     * @param ruc   The RUC to check for existence.
     * @return  True if a company with the specified RUC exists, false otherwise.
     */
    boolean existsByRUC(String ruc);
}
