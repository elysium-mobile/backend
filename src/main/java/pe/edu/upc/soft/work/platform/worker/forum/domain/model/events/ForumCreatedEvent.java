package pe.edu.upc.soft.work.platform.worker.forum.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.CompanyId;

/**
 * ForumCreatedEvent
 * Event triggered when a new Forum is successfully created.
 */
@Getter
public class ForumCreatedEvent extends ApplicationEvent {
    /** The ID of the created forum. */
    private final Long forumId;
    /** The company ID associated with the forum. */
    private final CompanyId companyId;
    /** The title of the created forum. */
    private final String title;

    /**
     * ForumCreatedEvent Constructor
     * @param source    the source of the event
     * @param forumId   the ID of the created forum
     * @param companyId the company ID associated with the forum
     * @param title     the title of the forum
     */
    public ForumCreatedEvent(Object source, Long forumId, CompanyId companyId, String title) {
        super(source);
        this.forumId = forumId;
        this.companyId = companyId;
        this.title = title;
    }
}
