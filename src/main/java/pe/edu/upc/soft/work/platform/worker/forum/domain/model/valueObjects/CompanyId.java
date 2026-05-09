package pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record CompanyId(Long companyId) {

    public CompanyId{
        if(companyId==null)
        {
            throw new IllegalArgumentException("[companyId] must not be null");
        }
    }
}
