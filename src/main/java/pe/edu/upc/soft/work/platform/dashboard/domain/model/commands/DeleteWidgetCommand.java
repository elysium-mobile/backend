package pe.edu.upc.soft.work.platform.dashboard.domain.model.commands;

/**
 * Command to delete a Widget
 */
public record DeleteWidgetCommand(Long widgetId) {

    /**
     * Constructor with validation
     */
    public DeleteWidgetCommand {
        if (widgetId == null || widgetId <= 0) {
            throw new IllegalArgumentException("[DeleteWidgetCommand] widgetId must be a positive number");
        }
    }
}
