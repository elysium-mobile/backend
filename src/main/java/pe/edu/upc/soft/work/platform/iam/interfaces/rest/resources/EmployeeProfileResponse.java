package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * EmployeeProfileResponse record to represent employee profile data in API responses.
 * @param starStart the start date of the employee
 * @param position the position of the employee
 * @param salary the salary of the employee
 * @param workOfTeamId the unique identifier of the work team the employee belongs to
 * @param UserAccountId the unique identifier of the user account associated with the employee
 */
public record EmployeeProfileResponse(

        Long employeeProfileId,

        @JsonProperty("dateStart")
        Date starStart,

        @JsonProperty("position")
        String position,

        @JsonProperty("salary")
        Integer salary,

        @JsonProperty("work_of_team_id")
        Long workOfTeamId,

        @JsonProperty("user_account_id")
        Long UserAccountId

) {
}
