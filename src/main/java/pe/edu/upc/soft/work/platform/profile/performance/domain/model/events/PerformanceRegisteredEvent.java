package pe.edu.upc.soft.work.platform.profile.performance.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.valueobjects.EmployeeProfileId;

/**
 * PerformanceRegisteredEvent
 * Event triggered when a new Performance evaluation is successfully registered.
 */
@Getter
public class PerformanceRegisteredEvent extends ApplicationEvent {
    /** The ID of the registered performance evaluation. */
    private final Long performanceId;
    /** The employee profile associated with the performance. */
    private final EmployeeProfileId employeeProfileId;
    /** The classification/score assigned. */
    private final Integer classification;

    /**
     * PerformanceRegisteredEvent Constructor
     * @param source            the source of the event
     * @param performanceId     the ID of the registered performance
     * @param employeeProfileId the employee profile ID
     * @param classification    the classification score
     */
    public PerformanceRegisteredEvent(Object source, Long performanceId, EmployeeProfileId employeeProfileId, Integer classification) {
        super(source);
        this.performanceId = performanceId;
        this.employeeProfileId = employeeProfileId;
        this.classification = classification;
    }
}
