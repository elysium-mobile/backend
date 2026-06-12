package pe.edu.upc.soft.work.platform.dashboard.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * CompanyDeletedEvent
 * Event triggered when a Company is deleted, so other bounded contexts
 * (e.g. Dashboard) can react and clean up orphaned data.
 */
@Getter
public class CompanyDeletedEvent extends ApplicationEvent {
    /** The ID of the deleted Company. */
    private final Long companyId;

    /**
     * CompanyDeletedEvent Constructor
     * @param source    the source of the event
     * @param companyId the ID of the deleted company
     */
    public CompanyDeletedEvent(Object source, Long companyId) {
        super(source);
        this.companyId = companyId;
    }
}
