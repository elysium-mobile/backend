package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

/**
 * Response object for a Widget.
 * @param widgetId  the ID of the widget
 * @param title the title of the widget
 * @param refreshPeriod the refresh period of the widget in seconds
 * @param dashboardId the ID of the dashboard to which the widget belongs
 */
public record WidgetResponse(

        Long widgetId,
        String title,
        Integer refreshPeriod,
        Long dashboardId
) {
}
