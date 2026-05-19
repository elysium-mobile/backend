package pe.edu.upc.soft.work.platform.dashboard.domain.model.commands;

/**
 * Command to delete a Dashboard
 */
public record DeleteDashboardCommand(Long dashboardId) {

    /**
     * Constructor with validation
     */
    public DeleteDashboardCommand {
        if (dashboardId == null || dashboardId <= 0) {
            throw new IllegalArgumentException("[DeleteDashboardCommand] dashboardId must be a positive number");
        }
    }
}
