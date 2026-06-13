package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request body for updating a work team.
 * @param teamName the new name of the work team
 * @param leaderOfTeam  the new leader of the work team
 * @param unitOfWorkId  the ID of the unit of work to which the work team belongs
 */
public record UpdateWorkTeamRequest(

        @NotNull
        @NotBlank
        @JsonProperty("teamName")
        String teamName,
        @NotNull
        @NotBlank
        @JsonProperty("leaderOfTeam")
        String leaderOfTeam,
        @NotNull
        @NotBlank
        @JsonProperty("unitOfWorkId")
        Long unitOfWorkId

) {
}
