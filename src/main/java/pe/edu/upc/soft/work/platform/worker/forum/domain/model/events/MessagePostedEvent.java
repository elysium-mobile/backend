package pe.edu.upc.soft.work.platform.worker.forum.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.UserAccountId;

/**
 * MessagePostedEvent
 * Event triggered when a new Message is successfully posted in the forum.
 */
@Getter
public class MessagePostedEvent extends ApplicationEvent {
    /** The ID of the posted message. */
    private final Long messageId;
    /** The ID of the thread where the message was posted. */
    private final Long threadId;
    /** The user account ID of the author. */
    private final UserAccountId userAccountId;

    /**
     * MessagePostedEvent Constructor
     * @param source        the source of the event
     * @param messageId     the ID of the posted message
     * @param threadId      the ID of the thread
     * @param userAccountId the author's user account ID
     */
    public MessagePostedEvent(Object source, Long messageId, Long threadId, UserAccountId userAccountId) {
        super(source);
        this.messageId = messageId;
        this.threadId = threadId;
        this.userAccountId = userAccountId;
    }
}
