package pe.edu.upc.soft.work.platform.iam.domain.model.commands;

/**
 * Command to delete an employee profile.
 * @param employeeProfileId the identifier of the employee profile to be deleted
 */
public record DeleteEmployeeProfileCommand(Long employeeProfileId) {
}
