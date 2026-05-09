package pe.edu.upc.soft.work.platform.dashboard.domain.model.commands;

/**
 * Command to delete a UnitOfWork
 */
public record DeleteUnitOfWorkCommand(Long unitofworkId) {

    /**
     * Constructor with validation
     */
    public DeleteUnitOfWorkCommand {
        if (unitofworkId == null || unitofworkId <= 0) {
            throw new IllegalArgumentException("[DeleteUnitOfWorkCommand] unitofworkId must be a positive number");
        }
    }
}
