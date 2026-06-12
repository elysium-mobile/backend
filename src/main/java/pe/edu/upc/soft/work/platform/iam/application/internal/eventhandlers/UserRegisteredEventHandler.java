package pe.edu.upc.soft.work.platform.iam.application.internal.eventhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.iam.domain.model.events.UserRegisteredEvent;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetUserByIdQuery;
import pe.edu.upc.soft.work.platform.iam.domain.services.UserQueryService;

/**
 * Event handler responsible for reacting to a successful UserRegisteredEvent.
 */
@Service
public class UserRegisteredEventHandler {

    private final UserQueryService userQueryService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRegisteredEventHandler.class);

    /**
     * Constructor for UserRegisteredEventHandler.
     * @param userQueryService service to query the User aggregate
     */
    public UserRegisteredEventHandler(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    /**
     * Handles the UserRegisteredEvent after a new user has been successfully registered.
     * @param event the UserRegisteredEvent containing the ID of the registered user
     */
    @EventListener
    public void on(UserRegisteredEvent event) {
        var getUserByIdQuery = new GetUserByIdQuery(event.getUserId());
        var user = userQueryService.handle(getUserByIdQuery);

        if (user.isPresent()) {
            LOGGER.info("User successfully registered with ID: {}", event.getUserId());
        } else {
            LOGGER.warn("Error: The user with ID could not be found after registration: {}", event.getUserId());
        }
    }
}
