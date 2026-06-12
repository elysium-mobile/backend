package pe.edu.upc.soft.work.platform.dashboard.domain.services;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates.Company;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.*;

import java.util.Optional;

/**
 * Service interface for handling Company-related commands.
 */
public interface CompanyCommandService {

    /**
     * Handles the creation of a new Company.
     */
    Long handle(CreateCompanyCommand command);

    /**
     * Handles the update of an existing Company.
     */
    Optional<Company> handle(UpdateCompanyCommand command);

    /**
     * Handles the deletion of an existing Company.
     */
    void handle(DeleteCompanyCommand command);

    void handle (AddAreaCompanyToCompanyCommand command);

    void handle (AddEmployeesToCompanyCommand command);
}
