package pe.edu.upc.soft.work.platform.worker.forum.application.internal.eventhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.events.ForumCreatedEvent;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetForumByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.ForumQueryService;

/**
 * Event handler responsible for reacting to a successful ForumCreatedEvent.
 */
@Service
public class ForumCreatedEventHandler {

    private final ForumQueryService forumQueryService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ForumCreatedEventHandler.class);

    /**
     * Constructor for ForumCreatedEventHandler.
     * @param forumQueryService service to query the Forum aggregate
     */
    public ForumCreatedEventHandler(ForumQueryService forumQueryService) {
        this.forumQueryService = forumQueryService;
    }

    /**
     * Handles the ForumCreatedEvent after a new forum has been successfully created.
     * @param event the ForumCreatedEvent containing forum details
     */
    @EventListener
    public void on(ForumCreatedEvent event) {
        var getForumByIdQuery = new GetForumByIdQuery(event.getForumId());
        var forum = forumQueryService.handle(getForumByIdQuery);

        if (forum.isPresent()) {
            LOGGER.info("Forum successfully created with ID: {}, title: '{}' for Company ID: {}",
                    event.getForumId(), event.getTitle(), event.getCompanyId());
        } else {
            LOGGER.warn("Error: Forum with ID {} could not be found after creation.", event.getForumId());
        }
    }
}
