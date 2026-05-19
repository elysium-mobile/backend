package pe.edu.upc.soft.work.platform.feedback.domain.model.commands;

/**
 * Command to delete a Survey
 */
public record DeleteSurveyCommand(Long surveyId) {

    /**
     * Constructor with validation
     */
    public DeleteSurveyCommand {
        if (surveyId == null || surveyId <= 0) {
            throw new IllegalArgumentException("[DeleteSurveyCommand] surveyId must be a positive number");
        }
    }
}
