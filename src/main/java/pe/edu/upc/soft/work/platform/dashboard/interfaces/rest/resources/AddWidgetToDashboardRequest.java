package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;


/** * Request body for adding a widget to a dashboard.
 * @param widgetId the ID of the widget to be added to the dashboard
 */
public record AddWidgetToDashboardRequest(
        @NotNull
        @JsonProperty("widgetId")
        Long widgetId
) {
}
