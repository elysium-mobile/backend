package pe.edu.upc.soft.work.platform.feedback.application.internal.eventhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.feedback.domain.model.events.SurveyResponseRegisteredEvent;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetSurveyResponseByIdQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.services.SurveyResponseQueryService;

/**
 * Event handler responsible for reacting to a successful SurveyResponseRegisteredEvent.
 */
@Service
public class SurveyResponseRegisteredEventHandler {

    private final SurveyResponseQueryService surveyResponseQueryService;
    private static final Logger LOGGER = LoggerFactory.getLogger(SurveyResponseRegisteredEventHandler.class);

    /**
     * Constructor for SurveyResponseRegisteredEventHandler.
     * @param surveyResponseQueryService service to query the SurveyResponse aggregate
     */
    public SurveyResponseRegisteredEventHandler(SurveyResponseQueryService surveyResponseQueryService) {
        this.surveyResponseQueryService = surveyResponseQueryService;
    }

    /**
     * Handles the SurveyResponseRegisteredEvent after a new survey response has been registered.
     * @param event the SurveyResponseRegisteredEvent containing response and survey IDs
     */
    @EventListener
    public void on(SurveyResponseRegisteredEvent event) {
        var getSurveyResponseByIdQuery = new GetSurveyResponseByIdQuery(event.getSurveyResponseId());
        var surveyResponse = surveyResponseQueryService.handle(getSurveyResponseByIdQuery);

        if (surveyResponse.isPresent()) {
            LOGGER.info("SurveyResponse successfully registered with ID: {} for Survey ID: {}",
                    event.getSurveyResponseId(), event.getSurveyId());
        } else {
            LOGGER.warn("Error: SurveyResponse with ID {} could not be found after registration.", event.getSurveyResponseId());
        }
    }
}
