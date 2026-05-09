package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record UpdateEmployeeProfileRequest(
        @NotNull
        @NotBlank
        @JsonProperty("start_date")
        Date dateStart,

        @NotNull
        @NotBlank
        @JsonProperty("position")
        String position,

        @NotNull
        @NotBlank
        @JsonProperty("salary")
        Integer salary,

        @NotNull
        @NotBlank
        @JsonProperty("work_of_team_id")
        Long workOfTeamId,

        @NotNull
        @NotBlank
        @JsonProperty("user_account_id")
        Long UserAccountId

) {
}
