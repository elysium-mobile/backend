package pe.edu.upc.soft.work.platform.profile.performance.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record EmployeeProfileId(Long employeeProfileId) {

    public EmployeeProfileId {
        if(employeeProfileId == null){
            throw new IllegalArgumentException("[employeeProfileId] must not be null");
        }
    }
}
