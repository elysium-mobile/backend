package pe.edu.upc.soft.work.platform.profile.performance.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@Embeddable
public record RRHHProfileId(Long rrhhProfileId) {

    public RRHHProfileId {
        if (rrhhProfileId == null){
            throw new IllegalArgumentException("[rrhhProfile] must not be null");
        }
    }
}
