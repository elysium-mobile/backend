package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotNull;


/** * Request body for adding a widget to a dashboard.
 * @param widgetId the ID of the widget to be added to the dashboard
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AddWidgetToDashboardRequest(
        @NotNull
        Long widgetId
) {
}
