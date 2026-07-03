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
        @NotBlank
        Long employeeProfileId,
        @NotNull
        @NotBlank
        Date dateTime,
        @NotNull
        @NotBlank
        Integer classification
) {}
