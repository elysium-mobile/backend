package pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands;

import pe.edu.upc.soft.work.platform.profile.performance.domain.model.valueobjects.EmployeeProfileId;

import java.util.Objects;
import java.util.Date;

/**
 * Command to update an existing Performance
 */
public record UpdatePerformanceCommand(Long performanceId, EmployeeProfileId employeeProfileId, Date dateTime, Integer classification) {

    /**
     * Constructor with validation
     */
    public UpdatePerformanceCommand {
        Objects.requireNonNull(performanceId, "[UpdatePerformanceCommand] performanceId must not be null");
    }
}
