package pe.edu.upc.soft.work.platform.profile.performance.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value Object representing the identifier of a employee profile.
 * @param employeeProfileId the identifier of the employee
 */
@Embeddable
public record EmployeeProfileId(Long employeeProfileId) {

    public EmployeeProfileId {
        if(employeeProfileId == null){
            throw new IllegalArgumentException("[employeeProfileId] must not be null");
        }
    }
}
