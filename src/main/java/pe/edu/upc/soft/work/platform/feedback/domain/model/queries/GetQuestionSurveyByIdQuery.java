package pe.edu.upc.soft.work.platform.feedback.domain.model.queries;

/**
 * Query to retrieve a QuestionSurvey by their unique identifier.
 */
public record GetQuestionSurveyByIdQuery(Long questionsurveyId) {

    /**
     * Constructor to validate the questionsurveyId parameter.
     */
    public GetQuestionSurveyByIdQuery {
        if (questionsurveyId == null || questionsurveyId <= 0) {
            throw new IllegalArgumentException("QuestionSurvey ID must be a positive number.");
        }
    }
}
