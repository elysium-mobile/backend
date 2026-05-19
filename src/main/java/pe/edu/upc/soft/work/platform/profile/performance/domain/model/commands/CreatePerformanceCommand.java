package pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands;

import pe.edu.upc.soft.work.platform.profile.performance.domain.model.valueobjects.EmployeeProfileId;

import java.util.Objects;
import java.util.Date;

/**
 * Command to create a new Performance
 */
public record CreatePerformanceCommand(EmployeeProfileId employeeProfileId, Date dateTime, Integer classification) {

    /**
     * Constructor with validation
     */
    public CreatePerformanceCommand {
        Objects.requireNonNull(employeeProfileId, "[CreatePerformanceCommand] employeeProfileId must not be null");
        Objects.requireNonNull(dateTime, "[CreatePerformanceCommand] dateTime must not be null");
        Objects.requireNonNull(classification, "[CreatePerformanceCommand] classification must not be null");
    }
}
