package pe.edu.upc.soft.work.platform.iam.domain.model.queries;


/**
 * Query to retrieve an employee profile by its identifier.
 * @param employeeId the identifier of the employee profile to be retrieved
 */
public record GetEmployeeProfileByIdQuery(Long employeeId) {

    /**
     * Constructor with validations.
     * @param employeeId the identifier of the employee profile to be retrieved
     */
    public GetEmployeeProfileByIdQuery {
        if (employeeId == null) {
            throw new IllegalArgumentException("[GetEmployeeProfileByIdQuery] employee id must not be null");
        }
    }
}
