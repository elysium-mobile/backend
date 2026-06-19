package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request body for creating a new work team.
 * @param teamName the name of the work team to be created
 * @param leaderOfTeam  the name of the leader of the work team
 * @param unitOfWorkId  the ID of the unit of work to which the work team will be assigned
 */
public record CreateWorkTeamRequest(

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
