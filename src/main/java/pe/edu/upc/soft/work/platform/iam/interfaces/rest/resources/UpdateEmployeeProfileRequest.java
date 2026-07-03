package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UpdateEmployeeProfileRequest(
        @NotNull
        @NotBlank
        Date dateStart,

        @NotNull
        @NotBlank
        String position,

        @NotNull
        @NotBlank
        Integer salary,

        @NotNull
        @NotBlank
        Long workOfTeamId,

        @NotNull
        @NotBlank
        Long UserAccountId

) {
}
