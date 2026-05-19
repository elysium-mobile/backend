package pe.edu.upc.soft.work.platform.feedback.domain.model.queries;

/**
 * Query to retrieve a Survey by their unique identifier.
 */
public record GetSurveyByIdQuery(Long surveyId) {

    /**
     * Constructor to validate the surveyId parameter.
     */
    public GetSurveyByIdQuery {
        if (surveyId == null || surveyId <= 0) {
            throw new IllegalArgumentException("Survey ID must be a positive number.");
        }
    }
}
