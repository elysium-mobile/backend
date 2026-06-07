package pe.edu.upc.soft.work.platform.dashboard.domain.model.commands;

import java.util.Objects;
import java.util.Date;

/**
 * Command to create a new Widget
 */
public record CreateWidgetCommand(String title, Integer refreshPeriod,Long dashboardId ) {

    /**
     * Constructor with validation
     */
    public CreateWidgetCommand {
        Objects.requireNonNull(title, "[CreateWidgetCommand] title must not be null");
        Objects.requireNonNull(refreshPeriod, "[CreateWidgetCommand] refreshPeriod must not be null");
    }
}
