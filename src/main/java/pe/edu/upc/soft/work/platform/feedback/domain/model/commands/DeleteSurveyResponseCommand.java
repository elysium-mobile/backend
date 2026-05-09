package pe.edu.upc.soft.work.platform.feedback.domain.model.commands;

/**
 * Command to delete a SurveyResponse
 */
public record DeleteSurveyResponseCommand(Long surveyresponseId) {

    /**
     * Constructor with validation
     */
    public DeleteSurveyResponseCommand {
        if (surveyresponseId == null || surveyresponseId <= 0) {
            throw new IllegalArgumentException("[DeleteSurveyResponseCommand] surveyresponseId must be a positive number");
        }
    }
}
