package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateWidgetRequest(
        @NotNull
        @NotBlank
        String title,

        @NotNull
        @NotBlank
        @JsonProperty("refreshPeriod")
        Integer refreshPeriod

) {
}
