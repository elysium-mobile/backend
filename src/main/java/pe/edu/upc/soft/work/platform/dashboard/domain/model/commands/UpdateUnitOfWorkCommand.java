package pe.edu.upc.soft.work.platform.dashboard.domain.model.commands;

import java.util.Objects;
import java.util.Date;

/**
 * Command to update an existing UnitOfWork
 */
public record UpdateUnitOfWorkCommand(Long unitofworkId, String name) {

    /**
     * Constructor with validation
     */
    public UpdateUnitOfWorkCommand {
        Objects.requireNonNull(unitofworkId, "[UpdateUnitOfWorkCommand] unitofworkId must not be null");
    }
}
