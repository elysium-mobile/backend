package pe.edu.upc.soft.work.platform.profile.performance.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

/**
 * Value Object representing the identifier of a RRHHProfile.
 * @param rrhhProfileId the identifier of the RRHHProfile
 */
@Embeddable
public record RRHHProfileId(Long rrhhProfileId) {

    public RRHHProfileId {
        if (rrhhProfileId == null){
            throw new IllegalArgumentException("[rrhhProfile] must not be null");
        }
    }
}
