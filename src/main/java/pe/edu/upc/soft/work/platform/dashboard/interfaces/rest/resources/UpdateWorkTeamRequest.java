package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateWorkTeamRequest(

        @NotNull
        @NotBlank
        String teamName,
        @NotNull
        @NotBlank
        String leaderOfTeam
) {
}
