package pe.edu.upc.soft.work.platform.dashboard.domain.model.commands;

/**
 *  Command to add a Widget to a Dashboard.
 * @param widgetId  the ID of the Widget to be added
 * @param dashboardId the ID of the Dashboard to which the Widget will be added
 */
public record AddWidgetToDashboardCommand(Long widgetId, Long dashboardId) {
}
