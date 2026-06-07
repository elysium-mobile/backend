package pe.edu.upc.soft.work.platform.dashboard.application.internal.eventhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.events.WorkTeamCreatedEvent;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetWorkTeamByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.WorkTeamQueryService;

/**
 * Event handler responsible for reacting to a successful WorkTeamCreatedEvent.
 */
@Service
public class WorkTeamCreatedEventHandler {

    private final WorkTeamQueryService workTeamQueryService;
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkTeamCreatedEventHandler.class);

    /**
     * Constructor for WorkTeamCreatedEventHandler.
     * @param workTeamQueryService service to query the WorkTeam aggregate
     */
    public WorkTeamCreatedEventHandler(WorkTeamQueryService workTeamQueryService) {
        this.workTeamQueryService = workTeamQueryService;
    }

    /**
     * Handles the WorkTeamCreatedEvent after a new work team has been successfully created.
     * @param event the WorkTeamCreatedEvent containing the ID of the work team and its company
     */
    @EventListener
    public void on(WorkTeamCreatedEvent event) {
        var getWorkTeamByIdQuery = new GetWorkTeamByIdQuery(event.getWorkTeamId());
        var workTeam = workTeamQueryService.handle(getWorkTeamByIdQuery);

        if (workTeam.isPresent()) {
            LOGGER.info("WorkTeam successfully created with ID: {} for Company ID: {}",
                    event.getWorkTeamId(), event.getCompanyId());
        } else {
            LOGGER.warn("Error: WorkTeam with ID {} could not be found after creation.", event.getWorkTeamId());
        }
    }
}
