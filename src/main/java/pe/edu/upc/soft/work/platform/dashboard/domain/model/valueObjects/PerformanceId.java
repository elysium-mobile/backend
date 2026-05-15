package pe.edu.upc.soft.work.platform.dashboard.domain.model.valueObjects;

import jakarta.persistence.Embeddable;

/**
 * Value Object representing the identifier of a performance.
 * @param performanceId the identifier of the Performance
 */
@Embeddable
public record PerformanceId(Long performanceId) {

    public PerformanceId{
        if(performanceId == null)
        {
            throw new IllegalArgumentException("[performanceId] must not be null");
        }
    }
}
