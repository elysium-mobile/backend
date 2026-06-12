package pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands;

import pe.edu.upc.soft.work.platform.profile.performance.domain.model.aggregates.CommentEmployee;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.valueobjects.EmployeeProfileId;

import java.util.List;
import java.util.Objects;
import java.util.Date;

/**
 * Command to create a new Performance
 */
public record CreatePerformanceCommand(EmployeeProfileId employeeProfileId, Date dateTime, Integer classification,
                                       List<CommentEmployee> commentEmployeeList) {

    /**
     * Constructor with validation
     */
    public CreatePerformanceCommand {
        Objects.requireNonNull(employeeProfileId, "[CreatePerformanceCommand] employeeProfileId must not be null");
        Objects.requireNonNull(dateTime, "[CreatePerformanceCommand] dateTime must not be null");
        Objects.requireNonNull(classification, "[CreatePerformanceCommand] classification must not be null");
    }
}
