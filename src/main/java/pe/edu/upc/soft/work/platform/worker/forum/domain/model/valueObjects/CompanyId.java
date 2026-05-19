package pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects;

import jakarta.persistence.Embeddable;

/**
 * Value Object representing the identifier of a Company.
 * @param companyId the identifier of the Company Context
 */
@Embeddable
public record CompanyId(Long companyId) {

    public CompanyId{
        if(companyId==null)
        {
            throw new IllegalArgumentException("[companyId] must not be null");
        }
    }
}
