package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
