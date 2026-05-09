package pe.edu.upc.soft.work.platform.feedback.domain.model.valueObjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record EmployeeProfileId(Long employeeProfileId) {

    public EmployeeProfileId {
        if(employeeProfileId == null){
            throw new IllegalArgumentException("[employeeProfileId] must noy be null");
        }
    }
}
