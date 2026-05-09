package pe.edu.upc.soft.work.platform.dashboard.domain.services;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.UnitOfWork;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateUnitOfWorkCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateUnitOfWorkCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.DeleteUnitOfWorkCommand;

import java.util.Optional;

/**
 * Service interface for handling UnitOfWork-related commands.
 */
public interface UnitOfWorkCommandService {

    /**
     * Handles the creation of a new UnitOfWork.
     */
    Long handle(CreateUnitOfWorkCommand command);

    /**
     * Handles the update of an existing UnitOfWork.
     */
    Optional<UnitOfWork> handle(UpdateUnitOfWorkCommand command);

    /**
     * Handles the deletion of an existing UnitOfWork.
     */
    void handle(DeleteUnitOfWorkCommand command);
}
