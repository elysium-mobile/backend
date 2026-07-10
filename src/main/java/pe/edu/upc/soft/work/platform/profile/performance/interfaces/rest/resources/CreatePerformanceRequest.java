package pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.valueobjects.EmployeeProfileId;

import java.util.Date;

/**
 * Request object for creating a new Performance.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreatePerformanceRequest(
        @NotNull
        Long employeeProfileId,
        @NotNull
        Date dateTime,
        @NotNull
        Integer classification
) {}
