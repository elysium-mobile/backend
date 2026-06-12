package pe.edu.upc.soft.work.platform.feedback.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * SurveyResponseRegisteredEvent
 * Event triggered when a new SurveyResponse is successfully registered.
 */
@Getter
public class SurveyResponseRegisteredEvent extends ApplicationEvent {
    /** The ID of the registered survey response. */
    private final Long surveyResponseId;
    /** The ID of the survey that was responded to. */
    private final Long surveyId;

    /**
     * SurveyResponseRegisteredEvent Constructor
     * @param source           the source of the event
     * @param surveyResponseId the ID of the registered survey response
     * @param surveyId         the ID of the associated survey
     */
    public SurveyResponseRegisteredEvent(Object source, Long surveyResponseId, Long surveyId) {
        super(source);
        this.surveyResponseId = surveyResponseId;
        this.surveyId = surveyId;
    }
}
