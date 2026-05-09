package pe.edu.upc.soft.work.platform.feedback.domain.model.commands;

import pe.edu.upc.soft.work.platform.feedback.domain.model.valueObjects.QuestionType;

import java.util.Objects;
import java.util.Date;

/**
 * Command to update an existing QuestionSurvey
 */
public record UpdateQuestionSurveyCommand(Long questionsurveyId, String textQuestion, QuestionType questionType) {

    /**
     * Constructor with validation
     */
    public UpdateQuestionSurveyCommand {
        Objects.requireNonNull(questionsurveyId, "[UpdateQuestionSurveyCommand] questionsurveyId must not be null");
    }
}
