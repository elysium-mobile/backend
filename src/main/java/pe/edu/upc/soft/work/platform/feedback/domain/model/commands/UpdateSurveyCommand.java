package pe.edu.upc.soft.work.platform.feedback.domain.model.commands;

import pe.edu.upc.soft.work.platform.feedback.domain.model.valueObjects.TargetType;

import java.util.Objects;
import java.util.Date;

/**
 * Command to update an existing Survey
 */
public record UpdateSurveyCommand(Long surveyId, String title, String description, TargetType targetType, Date expirationTime) {

    /**
     * Constructor with validation
     */
    public UpdateSurveyCommand {
        Objects.requireNonNull(surveyId, "[UpdateSurveyCommand] surveyId must not be null");
    }
}
