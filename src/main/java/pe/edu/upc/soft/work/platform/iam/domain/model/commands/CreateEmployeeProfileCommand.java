package pe.edu.upc.soft.work.platform.iam.domain.model.commands;

import pe.edu.upc.soft.work.platform.iam.domain.model.valueobjects.WorkOfTeamId;

import java.util.Date;
import java.util.Objects;

/**
 * Command for creating an employee profile.
 * @param dateStart the date when the employee started working
 * @param position the position of the employee
 * @param salary the salary of the employee
 * @param userAccountId the user account associated with the employee profile
 * @param workOfTeamId the work of team id associated with the employee profile
 */
public record CreateEmployeeProfileCommand(Date dateStart, String position, Integer salary, Long userAccountId, WorkOfTeamId workOfTeamId) {

    /**
     * Constructor for CreateEmployeeProfileCommand.
     * @param dateStart the date when the employee started working
     * @param position the position of the employee
     * @param salary the salary of the employee
     * @param userAccountId the user account associated with the employee profile
     * @param workOfTeamId the work of team id associated with the employee profile
     */
    public CreateEmployeeProfileCommand {
        Objects.requireNonNull(dateStart, "[CreateEmployeeProfileCommand] date start must not be null");
        Objects.requireNonNull(position, "[CreateEmployeeProfileCommand] position must not be null");
        Objects.requireNonNull(salary, "[CreateEmployeeProfileCommand] salary must not be null");
        Objects.requireNonNull(userAccountId, "[CreateEmployeeProfileCommand] user account id must not be null");
            Objects.requireNonNull(workOfTeamId, "[CreateEmployeeProfileCommand] work of team id must not be null");
    }

}
