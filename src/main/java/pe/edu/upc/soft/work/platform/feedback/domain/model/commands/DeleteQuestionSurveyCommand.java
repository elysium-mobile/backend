package pe.edu.upc.soft.work.platform.feedback.domain.model.commands;

/**
 * Command to delete a QuestionSurvey
 */
public record DeleteQuestionSurveyCommand(Long questionsurveyId) {

    /**
     * Constructor with validation
     */
    public DeleteQuestionSurveyCommand {
        if (questionsurveyId == null || questionsurveyId <= 0) {
            throw new IllegalArgumentException("[DeleteQuestionSurveyCommand] questionsurveyId must be a positive number");
        }
    }
}
