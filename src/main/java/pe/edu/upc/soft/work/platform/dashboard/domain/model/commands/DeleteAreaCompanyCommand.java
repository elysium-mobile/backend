package pe.edu.upc.soft.work.platform.dashboard.domain.model.commands;

/**
 * Command to delete a AreaCompany
 */
public record DeleteAreaCompanyCommand(Long areacompanyId) {

    /**
     * Constructor with validation
     */
    public DeleteAreaCompanyCommand {
        if (areacompanyId == null || areacompanyId <= 0) {
            throw new IllegalArgumentException("[DeleteAreaCompanyCommand] areacompanyId must be a positive number");
        }
    }
}
