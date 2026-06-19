package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

/**
 * Request body for adding a work team to a unit of work.
 * @param workTeamId the ID of the work team to be added to the unit of work
 */
public record AddWorkTeamToUnitOFWorkRequest(
        @NotNull
        @JsonProperty("workTeamId")
        Long workTeamId
) {
}
