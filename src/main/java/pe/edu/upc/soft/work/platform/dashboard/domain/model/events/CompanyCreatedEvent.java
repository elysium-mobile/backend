package pe.edu.upc.soft.work.platform.dashboard.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * CompanyCreatedEvent
 * Event triggered when a new Company is successfully created.
 */
@Getter
public class CompanyCreatedEvent extends ApplicationEvent {
    /** The ID of the created Company. */
    private final Long companyId;
    /** The name of the created company. */
    private final String companyName;

    /**
     * CompanyCreatedEvent Constructor
     * @param source      the source of the event
     * @param companyId   the ID of the created company
     * @param companyName the name of the created company
     */
    public CompanyCreatedEvent(Object source, Long companyId, String companyName) {
        super(source);
        this.companyId = companyId;
        this.companyName = companyName;
    }
}
