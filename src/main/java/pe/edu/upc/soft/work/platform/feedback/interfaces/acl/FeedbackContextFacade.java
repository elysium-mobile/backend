package pe.edu.upc.soft.work.platform.feedback.interfaces.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetSurveyByIdQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.services.SurveyQueryService;

/**
 * Facade for the Feedback Bounded Context.
 * Exposes survey verification operations for other Bounded Contexts.
 */
@Service
public class FeedbackContextFacade {

    /**
     * Query service for surveys.
     */
    private final SurveyQueryService surveyQueryService;

    /**
     * Constructor for FeedbackContextFacade.
     *
     * @param surveyQueryService the survey query service
     */
    public FeedbackContextFacade(SurveyQueryService surveyQueryService) {
        this.surveyQueryService = surveyQueryService;
    }

    /**
     * Check if a survey exists by its ID.
     *
     * @param surveyId the ID of the survey
     * @return true if the survey exists, false otherwise
     */
    public boolean existsSurveyById(Long surveyId) {
        var query = new GetSurveyByIdQuery(surveyId);
        return this.surveyQueryService.handle(query).isPresent();
    }
}
