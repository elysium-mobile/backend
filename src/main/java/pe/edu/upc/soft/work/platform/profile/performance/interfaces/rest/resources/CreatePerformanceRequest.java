package pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.valueobjects.EmployeeProfileId;

import java.util.Date;

/**
 * Request object for creating a new Performance.
 */
public record CreatePerformanceRequest(
        @NotNull
        @NotBlank
        EmployeeProfileId employeeProfileId,
        @NotNull
        @NotBlank
        Date dateTime,
        @NotNull
        @NotBlank
        Integer classification
) {}
