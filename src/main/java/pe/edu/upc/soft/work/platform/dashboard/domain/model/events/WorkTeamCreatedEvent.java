package pe.edu.upc.soft.work.platform.dashboard.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * WorkTeamCreatedEvent
 * Event triggered when a new WorkTeam is successfully created.
 */
@Getter
public class WorkTeamCreatedEvent extends ApplicationEvent {
    /** The ID of the created WorkTeam. */
    private final Long workTeamId;
    /** The ID of the company associated with the work team. */
    private final Long companyId;

    /**
     * WorkTeamCreatedEvent Constructor
     * @param source      the source of the event
     * @param workTeamId  the ID of the created work team
     * @param companyId   the ID of the associated company
     */
    public WorkTeamCreatedEvent(Object source, Long workTeamId, Long companyId) {
        super(source);
        this.workTeamId = workTeamId;
        this.companyId = companyId;
    }
}
