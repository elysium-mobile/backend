package pe.edu.upc.soft.work.platform.profile.performance.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * CommentEmployeeAddedEvent
 * Event triggered when a comment is successfully added for an employee.
 */
@Getter
public class CommentEmployeeAddedEvent extends ApplicationEvent {
    /** The ID of the added comment. */
    private final Long commentEmployeeId;
    /** The ID of the performance evaluation to which the comment belongs. */
    private final Long performanceId;

    /**
     * CommentEmployeeAddedEvent Constructor
     * @param source              the source of the event
     * @param commentEmployeeId   the ID of the added comment
     * @param performanceId       the ID of the associated performance evaluation
     */
    public CommentEmployeeAddedEvent(Object source, Long commentEmployeeId, Long performanceId) {
        super(source);
        this.commentEmployeeId = commentEmployeeId;
        this.performanceId = performanceId;
    }
}
