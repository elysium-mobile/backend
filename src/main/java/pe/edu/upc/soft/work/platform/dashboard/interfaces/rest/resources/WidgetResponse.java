package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

/**
 * Response object for a Widget.
 * @param widgetId  the ID of the widget
 * @param title the title of the widget
 * @param refreshPeriod the refresh period of the widget in seconds
 * @param dashboardId the ID of the dashboard to which the widget belongs
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WidgetResponse(

        Long widgetId,
        String title,
        Integer refreshPeriod,
        Long dashboardId
) {
}
