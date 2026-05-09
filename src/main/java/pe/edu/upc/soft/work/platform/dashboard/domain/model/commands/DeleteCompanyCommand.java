package pe.edu.upc.soft.work.platform.dashboard.domain.model.commands;

/**
 * Command to delete a Company
 */
public record DeleteCompanyCommand(Long companyId) {

    /**
     * Constructor with validation
     */
    public DeleteCompanyCommand {
        if (companyId == null || companyId <= 0) {
            throw new IllegalArgumentException("[DeleteCompanyCommand] companyId must be a positive number");
        }
    }
}
