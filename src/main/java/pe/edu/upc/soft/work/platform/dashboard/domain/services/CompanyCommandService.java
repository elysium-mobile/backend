package pe.edu.upc.soft.work.platform.dashboard.domain.services;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates.Company;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.DeleteCompanyCommand;

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
}
