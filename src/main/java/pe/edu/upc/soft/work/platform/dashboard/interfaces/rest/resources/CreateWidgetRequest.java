package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


/**
 * Request body for creating a new widget.
 * @param title the title of the widget
 * @param refreshPeriod the refresh period of the widget in seconds
 * @param dashboardId   the ID of the dashboard to which the widget belongs
 */
public record CreateWidgetRequest(
        @NotNull
        @NotBlank
        String title,

        @NotNull
        @NotBlank
        @JsonProperty("refreshPeriod")
        Integer refreshPeriod,

        @NotNull
        @NotBlank
        @JsonProperty("dashboardId")
        Long dashboardId
) {
}
