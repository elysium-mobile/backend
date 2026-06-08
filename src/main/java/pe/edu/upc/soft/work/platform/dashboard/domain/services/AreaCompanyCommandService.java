package pe.edu.upc.soft.work.platform.dashboard.domain.services;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.*;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.AreaCompany;

import java.util.Optional;

/**
 * Service interface for handling AreaCompany-related commands.
 */
public interface AreaCompanyCommandService {

    /**
     * Handles the creation of a new AreaCompany.
     */
    Long handle(CreateAreaCompanyCommand command);

    /**
     * Handles the update of an existing AreaCompany.
     */
    Optional<AreaCompany> handle(UpdateAreaCompanyCommand command);

    /**
     * Handles the deletion of an existing AreaCompany.
     */
    void handle(DeleteAreaCompanyCommand command);

    void handle(AddUnitOfWorkToAreaCompanyCommand command);
}
