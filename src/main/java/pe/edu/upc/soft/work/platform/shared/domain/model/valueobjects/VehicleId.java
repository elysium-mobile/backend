package pe.edu.upc.soft.work.platform.shared.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

import java.util.Objects;

/**
 * Value Object representing a Vehicle Identifier
 * @param vehicleId the unique identifier of the vehicle
 */
@Embeddable
public record VehicleId(Long vehicleId) {
    public VehicleId {
        if (Objects.isNull(vehicleId) || vehicleId < 0) {
            throw new IllegalArgumentException("Vehicle ID cannot be null or negative");
        }
    }

    /**
     * Default constructor for JPA
     */
    public VehicleId(){
        this(0L);
    }

    /**
     * Returns the vehicle ID value.
     *
     * @return the vehicle ID
     */
    public Long value() {
        return vehicleId;
    }
}
