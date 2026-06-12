package pe.edu.upc.soft.work.platform.feedback.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import pe.edu.upc.soft.work.platform.feedback.domain.model.valueObjects.TargetType;

/**
 * SurveyCreatedEvent
 * Event triggered when a new Survey is successfully created.
 */
@Getter
public class SurveyCreatedEvent extends ApplicationEvent {
    /** The ID of the created survey. */
    private final Long surveyId;
    /** The title of the created survey. */
    private final String title;
    /** The target type of the survey. */
    private final TargetType targetType;

    /**
     * SurveyCreatedEvent Constructor
     * @param source     the source of the event
     * @param surveyId   the ID of the created survey
     * @param title      the title of the survey
     * @param targetType the target type of the survey
     */
    public SurveyCreatedEvent(Object source, Long surveyId, String title, TargetType targetType) {
        super(source);
        this.surveyId = surveyId;
        this.title = title;
        this.targetType = targetType;
    }
}
