package pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects;

import jakarta.persistence.Embeddable;

/**
 * Value Object representing the identifier of a Area Company
 * @param areaCompanyId the identifier of the Company
 */
@Embeddable
public record AreaCompanyId(Long areaCompanyId) {

    public AreaCompanyId{
        if(areaCompanyId == null)
        {
            throw new IllegalArgumentException("[areaCompanyId] must noy be null");
        }
    }
}
