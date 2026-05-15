package pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.valueobjects.EmployeeProfileId;

import java.util.Date;

/**
 * Request object for updating an existing Performance.
 */
public record UpdatePerformanceRequest(
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
