package pe.edu.upc.soft.work.platform.worker.forum.application.internal.eventhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.events.MessagePostedEvent;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetMessageByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.MessageQueryService;

/**
 * Event handler responsible for reacting to a successful MessagePostedEvent.
 */
@Service
public class MessagePostedEventHandler {

    private final MessageQueryService messageQueryService;
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagePostedEventHandler.class);

    /**
     * Constructor for MessagePostedEventHandler.
     * @param messageQueryService service to query the Message aggregate
     */
    public MessagePostedEventHandler(MessageQueryService messageQueryService) {
        this.messageQueryService = messageQueryService;
    }

    /**
     * Handles the MessagePostedEvent after a new message has been successfully posted.
     * @param event the MessagePostedEvent containing message, thread, and user details
     */
    @EventListener
    public void on(MessagePostedEvent event) {
        var getMessageByIdQuery = new GetMessageByIdQuery(event.getMessageId());
        var message = messageQueryService.handle(getMessageByIdQuery);

        if (message.isPresent()) {
            LOGGER.info("Message successfully posted with ID: {} in Thread ID: {} by UserAccount ID: {}",
                    event.getMessageId(), event.getThreadId(), event.getUserAccountId());
        } else {
            LOGGER.warn("Error: Message with ID {} could not be found after posting.", event.getMessageId());
        }
    }
}
