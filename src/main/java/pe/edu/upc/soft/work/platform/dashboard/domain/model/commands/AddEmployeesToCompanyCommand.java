package pe.edu.upc.soft.work.platform.dashboard.domain.model.commands;

/**
 *  Command to add an Employee to a Company.
 * @param employeeId  the ID of the Employee to be added
 * @param companyId the ID of the Company to which the Employee will be added
 */
public record AddEmployeesToCompanyCommand(Long employeeId, Long companyId) {
}
