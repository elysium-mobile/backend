package pe.edu.upc.soft.work.platform.dashboard.domain.model.commands;

import java.util.Objects;
import java.util.Date;

/**
 * Command to update an existing Widget
 */
public record UpdateWidgetCommand(Long widgetId, String title, Integer refreshPeriod, Long dashboardId) {

    /**
     * Constructor with validation
     */
    public UpdateWidgetCommand {
        Objects.requireNonNull(widgetId, "[UpdateWidgetCommand] widgetId must not be null");
    }
}
