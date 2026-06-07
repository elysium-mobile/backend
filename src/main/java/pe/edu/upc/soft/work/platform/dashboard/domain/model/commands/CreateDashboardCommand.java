package pe.edu.upc.soft.work.platform.dashboard.domain.model.commands;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.Widget;

import java.util.List;
import java.util.Objects;
import java.util.Date;

/**
 * Command to create a new Dashboard
 */
public record CreateDashboardCommand(String ruc, List<Widget> widgetList) {

    /**
     * Constructor with validation
     */
    public CreateDashboardCommand {
        Objects.requireNonNull(ruc, "[CreateDashboardCommand] ruc must not be null");
    }
}
