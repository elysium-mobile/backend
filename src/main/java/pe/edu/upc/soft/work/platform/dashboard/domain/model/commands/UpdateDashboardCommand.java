package pe.edu.upc.soft.work.platform.dashboard.domain.model.commands;

import java.util.Objects;
import java.util.Date;

/**
 * Command to update an existing Dashboard
 */
public record UpdateDashboardCommand(Long dashboardId, String ruc) {

    /**
     * Constructor with validation
     */
    public UpdateDashboardCommand {
        Objects.requireNonNull(dashboardId, "[UpdateDashboardCommand] dashboardId must not be null");
    }
}
