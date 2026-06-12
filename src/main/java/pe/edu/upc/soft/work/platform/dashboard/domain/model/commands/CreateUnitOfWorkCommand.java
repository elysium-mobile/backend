package pe.edu.upc.soft.work.platform.dashboard.domain.model.commands;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.WorkTeam;

import java.util.List;
import java.util.Objects;
import java.util.Date;

/**
 * Command to create a new UnitOfWork
 */
public record CreateUnitOfWorkCommand(String name, List<WorkTeam> workTeamList) {

    /**
     * Constructor with validation
     */
    public CreateUnitOfWorkCommand {
        Objects.requireNonNull(name, "[CreateUnitOfWorkCommand] name must not be null");
    }
}
