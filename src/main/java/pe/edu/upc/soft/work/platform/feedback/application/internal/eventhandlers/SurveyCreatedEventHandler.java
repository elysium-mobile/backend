package pe.edu.upc.soft.work.platform.feedback.application.internal.eventhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.feedback.domain.model.events.SurveyCreatedEvent;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetSurveyByIdQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.services.SurveyQueryService;

/**
 * Event handler responsible for reacting to a successful SurveyCreatedEvent.
 */
@Service
public class SurveyCreatedEventHandler {

    private final SurveyQueryService surveyQueryService;
    private static final Logger LOGGER = LoggerFactory.getLogger(SurveyCreatedEventHandler.class);

    /**
     * Constructor for SurveyCreatedEventHandler.
     * @param surveyQueryService service to query the Survey aggregate
     */
    public SurveyCreatedEventHandler(SurveyQueryService surveyQueryService) {
        this.surveyQueryService = surveyQueryService;
    }

    /**
     * Handles the SurveyCreatedEvent after a new survey has been successfully created.
     * @param event the SurveyCreatedEvent containing survey details
     */
    @EventListener
    public void on(SurveyCreatedEvent event) {
        var getSurveyByIdQuery = new GetSurveyByIdQuery(event.getSurveyId());
        var survey = surveyQueryService.handle(getSurveyByIdQuery);

        if (survey.isPresent()) {
            LOGGER.info("Survey successfully created with ID: {}, title: '{}', targetType: {}",
                    event.getSurveyId(), event.getTitle(), event.getTargetType());
        } else {
            LOGGER.warn("Error: Survey with ID {} could not be found after creation.", event.getSurveyId());
        }
    }
}
