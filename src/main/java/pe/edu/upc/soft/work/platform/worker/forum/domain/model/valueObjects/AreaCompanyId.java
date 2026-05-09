package pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record AreaCompanyId(Long areaCompanyId) {

    public AreaCompanyId{
        if(areaCompanyId == null)
        {
            throw new IllegalArgumentException("[areaCompanyId] must noy be null");
        }
    }
}
