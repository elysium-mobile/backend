package pe.edu.upc.soft.work.platform.dashboard.domain.model.commands;

import java.util.Objects;
import java.util.Date;

/**
 * Command to create a new Dashboard
 */
public record CreateDashboardCommand(String ruc) {

    /**
     * Constructor with validation
     */
    public CreateDashboardCommand {
        Objects.requireNonNull(ruc, "[CreateDashboardCommand] ruc must not be null");
    }
}
