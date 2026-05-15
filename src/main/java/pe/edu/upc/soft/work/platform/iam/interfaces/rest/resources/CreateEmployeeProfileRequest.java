package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.shared.domain.model.valueobjects.UserAccountId;

import java.util.Date;

/**
 * Request record for creating a new employee profile.
 * @param dateStart the start date of the employee
 * @param position the position of the employee
 * @param salary the salary of the employee
 * @param workOfTeamId the ID of the work team the employee belongs to
 * @param UserAccountId the ID of the user account associated with the employee
 */
public record CreateEmployeeProfileRequest(
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

) { }
