package pe.edu.upc.soft.work.platform.feedback.domain.model.commands;

import pe.edu.upc.soft.work.platform.feedback.domain.model.valueObjects.TargetType;

import java.util.Objects;
import java.util.Date;

/**
 * Command to create a new Survey
 */
public record CreateSurveyCommand(String title, String description, TargetType targetType, Date expirationTime) {

    /**
     * Constructor with validation
     */
    public CreateSurveyCommand {
        Objects.requireNonNull(title, "[CreateSurveyCommand] title must not be null");
        Objects.requireNonNull(description, "[CreateSurveyCommand] description must not be null");
        Objects.requireNonNull(targetType, "[CreateSurveyCommand] targetType must not be null");
        Objects.requireNonNull(expirationTime, "[CreateSurveyCommand] expirationTime must not be null");
    }
}
