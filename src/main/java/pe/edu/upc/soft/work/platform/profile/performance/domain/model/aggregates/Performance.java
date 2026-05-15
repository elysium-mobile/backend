package pe.edu.upc.soft.work.platform.profile.performance.domain.model.aggregates;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
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
@Table(name = "performances")
public class Performance extends AuditableAbstractAggregateRoot<Performance> {

    @Getter
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "employeeProfileId", column = @Column(name = "employee_profile_id", nullable = false))
    })
    @JsonProperty("id_employee_profile")
    private EmployeeProfileId employeeProfileId;
    @Getter
    @Column(name = "date_time", nullable = false)
    private Date dateTime;
    @Getter
    @Column(name = "classification", nullable = false)
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
