package pe.edu.upc.soft.work.platform.iam.application.internal.eventhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.iam.domain.model.events.UserAccountCreatedEvent;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetUserAccountByIdQuery;
import pe.edu.upc.soft.work.platform.iam.domain.services.UserAccountQueryService;

/**
 * Event handler responsible for reacting to a successful UserAccountCreatedEvent.
 */
@Service
public class UserAccountCreatedEventHandler {

    private final UserAccountQueryService userAccountQueryService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserAccountCreatedEventHandler.class);

    /**
     * Constructor for UserAccountCreatedEventHandler.
     * @param userAccountQueryService service to query the UserAccount aggregate
     */
    public UserAccountCreatedEventHandler(UserAccountQueryService userAccountQueryService) {
        this.userAccountQueryService = userAccountQueryService;
    }

    /**
     * Handles the UserAccountCreatedEvent after a new user account has been successfully created.
     * @param event the UserAccountCreatedEvent containing relevant IDs
     */
    @EventListener
    public void on(UserAccountCreatedEvent event) {
        var getUserAccountByIdQuery = new GetUserAccountByIdQuery(event.getUserAccountId());
        var userAccount = userAccountQueryService.handle(getUserAccountByIdQuery);

        if (userAccount.isPresent()) {
            LOGGER.info("UserAccount successfully created with ID: {} for User ID: {} in Company ID: {}",
                    event.getUserAccountId(), event.getUserId(), event.getCompanyId());
        } else {
            LOGGER.warn("Error: UserAccount with ID {} could not be found after creation.", event.getUserAccountId());
        }
    }
}
