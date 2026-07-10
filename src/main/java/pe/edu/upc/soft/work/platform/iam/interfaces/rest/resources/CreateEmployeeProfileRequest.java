package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

/**
 * Request record for creating a new employee profile.
 * @param dateStart the start date of the employee
 * @param position the position of the employee
 * @param salary the salary of the employee
 * @param workOfTeamId the ID of the work team the employee belongs to
 * @param UserAccountId the ID of the user account associated with the employee
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateEmployeeProfileRequest(
        @NotNull
        Date dateStart,

        @NotNull
        @NotBlank
        String position,

        @NotNull
        Integer salary,

        @NotNull
        Long workOfTeamId,

        @NotNull
        Long UserAccountId

) { }
