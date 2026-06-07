package pe.edu.upc.soft.work.platform.profile.performance.application.internal.eventhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.events.CommentEmployeeAddedEvent;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.queries.GetCommentEmployeeByIdQuery;
import pe.edu.upc.soft.work.platform.profile.performance.domain.services.CommentEmployeeQueryService;

/**
 * Event handler responsible for reacting to a CommentEmployeeAddedEvent.
 */
@Service
public class CommentEmployeeAddedEventHandler {

    private final CommentEmployeeQueryService commentEmployeeQueryService;
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentEmployeeAddedEventHandler.class);

    /**
     * Constructor for CommentEmployeeAddedEventHandler.
     * @param commentEmployeeQueryService service to query the CommentEmployee aggregate
     */
    public CommentEmployeeAddedEventHandler(CommentEmployeeQueryService commentEmployeeQueryService) {
        this.commentEmployeeQueryService = commentEmployeeQueryService;
    }

    /**
     * Handles the CommentEmployeeAddedEvent after a comment has been added for an employee.
     * @param event the CommentEmployeeAddedEvent containing comment and performance IDs
     */
    @EventListener
    public void on(CommentEmployeeAddedEvent event) {
        var getCommentEmployeeByIdQuery = new GetCommentEmployeeByIdQuery(event.getCommentEmployeeId());
        var comment = commentEmployeeQueryService.handle(getCommentEmployeeByIdQuery);

        if (comment.isPresent()) {
            LOGGER.info("CommentEmployee successfully added with ID: {} for Performance ID: {}",
                    event.getCommentEmployeeId(), event.getPerformanceId());
        } else {
            LOGGER.warn("Error: CommentEmployee with ID {} could not be found after creation.", event.getCommentEmployeeId());
        }
    }
}
