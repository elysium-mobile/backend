package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;


import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import java.util.Date;

/**
 * EmployeeProfileResponse record to represent employee profile data in API responses.
 * @param starStart the start date of the employee
 * @param position the position of the employee
 * @param salary the salary of the employee
 * @param workOfTeamId the unique identifier of the work team the employee belongs to
 * @param UserAccountId the unique identifier of the user account associated with the employee
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record EmployeeProfileResponse(

        Long employeeProfileId,

        Date starStart,

        String position,

        Integer salary,

        Long workOfTeamId,

        Long UserAccountId

) {
}
