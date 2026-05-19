package pe.edu.upc.soft.work.platform.shared.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

import java.util.Objects;

/**
 * Value Object representing the identifier of an Auto Repair entity.
 * @param autoRepairId the unique identifier for the Auto Repair
 */
@Embeddable
public record AutoRepairId(Long autoRepairId) {

    /**
     * Constructor with validation.
     *
     * @param autoRepairId the unique identifier for the Auto Repair
     */
    public AutoRepairId {
        if (Objects.isNull(autoRepairId) || autoRepairId <= 0) {
            throw new IllegalArgumentException("[IdAutoRepair] idAutoRepair cannot be null or negative");
        }
    }

    /**
     * Default constructor for JPA
     */
    public AutoRepairId() {
        this(0L);
    }

    /**
     * Returns the auto repair ID value.
     *
     * @return the auto repair ID
     */
    public Long value() {
        return this.autoRepairId;
    }
}
