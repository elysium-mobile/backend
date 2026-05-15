package pe.edu.upc.soft.work.platform.feedback.domain.model.valueObjects;

import jakarta.persistence.Embeddable;

/**
 * Value Object representing the identifier of an employee profile
 * @param employeeProfileId the identifier of the EmployeeProfile
 */
@Embeddable
public record EmployeeProfileId(Long employeeProfileId) {

    public EmployeeProfileId {
        if(employeeProfileId == null){
            throw new IllegalArgumentException("[employeeProfileId] must noy be null");
        }
    }
}
