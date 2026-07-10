package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request body for updating a widget.
 * @param title the new title of the widget
 * @param refreshPeriod the new refresh period of the widget
 * @param dashboardId   the new dashboard ID of the widget
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UpdateWidgetRequest(
        @NotNull
        @NotBlank
        String title,

        @NotNull
        Integer refreshPeriod,

        @NotNull
        Long dashboardId

) {
}
