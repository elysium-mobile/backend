package pe.edu.upc.soft.work.platform.iam.domain.model.commands;

import pe.edu.upc.soft.work.platform.iam.domain.model.valueobjects.WorkOfTeamId;

import java.util.Date;
import java.util.Objects;

/**
 * Command class for updating an employee profile in the IAM system.
 * @param employeeProfileId the identifier of the employee profile to be updated
 * @param dateStart the date start of the employee profile
 * @param position the position of the employee
 * @param salary the salary of the employee
 * @param workOfTeamId the identifier of the work of team associated with the employee profile
 */
public record UpdateEmployeeProfileCommand(Long employeeProfileId, Date dateStart, String position, Integer salary, WorkOfTeamId workOfTeamId) {

    /**
     * Constructor for UpdateEmployeeProfileCommand that validates the input parameters.
     * @param employeeProfileId the identifier of the employee profile to be updated
     * @param dateStart the date start of the employee profile
     * @param position the position of the employee
     * @param salary the salary of the employee
     * @param workOfTeamId the identifier of the work of team associated with the employee profile
     */
    public UpdateEmployeeProfileCommand {
        Objects.requireNonNull(employeeProfileId, "Employee profile ID cannot be null");
        Objects.requireNonNull(dateStart, "Date start cannot be null");
        Objects.requireNonNull(position, "Position cannot be null");
        Objects.requireNonNull(salary, "Salary cannot be null");
        Objects.requireNonNull(workOfTeamId, "Work of team ID cannot be null");
    }
}
