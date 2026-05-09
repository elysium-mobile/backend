package pe.edu.upc.soft.work.platform.feedback.domain.model.queries;

/**
 * Query to retrieve a SurveyResponse by their unique identifier.
 */
public record GetSurveyResponseByIdQuery(Long surveyresponseId) {

    /**
     * Constructor to validate the surveyresponseId parameter.
     */
    public GetSurveyResponseByIdQuery {
        if (surveyresponseId == null || surveyresponseId <= 0) {
            throw new IllegalArgumentException("SurveyResponse ID must be a positive number.");
        }
    }
}
