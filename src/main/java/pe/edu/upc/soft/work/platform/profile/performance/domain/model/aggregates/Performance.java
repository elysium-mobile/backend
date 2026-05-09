package pe.edu.upc.soft.work.platform.profile.performance.domain.model.aggregates;

import jakarta.persistence.Entity;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.CreatePerformanceCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.UpdatePerformanceCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import java.util.Date;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.valueobjects.EmployeeProfileId;

/**
 * Performance aggregate root entity.
 */
@Entity
public class Performance extends AuditableAbstractAggregateRoot<Performance> {

    @Getter
    private EmployeeProfileId employeeProfileId;
    @Getter
    private Date dateTime;
    @Getter
    private Integer classification;

    /**
     * Default constructor for JPA.
     */
    public Performance() {}

    /**
     * Constructor to create a Performance from a CreatePerformanceCommand.
     * @param command the command containing performance details
     */
    public Performance(CreatePerformanceCommand command) {
        this.employeeProfileId = command.employeeProfileId();
        this.dateTime = command.dateTime();
        this.classification = command.classification();
    }

    /**
     * Updates the Performance with details from an UpdatePerformanceCommand.
     * @param command the command containing updated performance details
     */
    public void updatePerformance(UpdatePerformanceCommand command) {
        this.employeeProfileId = command.employeeProfileId();
        this.dateTime = command.dateTime();
        this.classification = command.classification();
    }
}
