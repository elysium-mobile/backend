package pe.edu.upc.soft.work.platform.profile.performance.application.internal.eventhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.events.PerformanceRegisteredEvent;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.queries.GetPerformanceByIdQuery;
import pe.edu.upc.soft.work.platform.profile.performance.domain.services.PerformanceQueryService;

/**
 * Event handler responsible for reacting to a successful PerformanceRegisteredEvent.
 */
@Service
public class PerformanceRegisteredEventHandler {

    private final PerformanceQueryService performanceQueryService;
    private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceRegisteredEventHandler.class);

    /**
     * Constructor for PerformanceRegisteredEventHandler.
     * @param performanceQueryService service to query the Performance aggregate
     */
    public PerformanceRegisteredEventHandler(PerformanceQueryService performanceQueryService) {
        this.performanceQueryService = performanceQueryService;
    }

    /**
     * Handles the PerformanceRegisteredEvent after a new performance evaluation has been registered.
     * @param event the PerformanceRegisteredEvent containing performance details
     */
    @EventListener
    public void on(PerformanceRegisteredEvent event) {
        var getPerformanceByIdQuery = new GetPerformanceByIdQuery(event.getPerformanceId());
        var performance = performanceQueryService.handle(getPerformanceByIdQuery);

        if (performance.isPresent()) {
            LOGGER.info("Performance successfully registered with ID: {} for EmployeeProfile ID: {} with classification: {}",
                    event.getPerformanceId(), event.getEmployeeProfileId(), event.getClassification());
        } else {
            LOGGER.warn("Error: Performance with ID {} could not be found after registration.", event.getPerformanceId());
        }
    }
}
