package pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources;

import pe.edu.upc.soft.work.platform.profile.performance.domain.model.valueobjects.EmployeeProfileId;

import java.util.Date;
import java.util.List;

/**
 * Response object representing a Performance in the system.
 */
public record PerformanceResponse(
        Long performanceId,
        Long employeeProfileId,
        Date dateTime,
        Integer classification,
        List<CommentEmployeeResponse> commentEmployees
) {}
