package pe.edu.upc.soft.work.platform.feedback.domain.model.commands;

import pe.edu.upc.soft.work.platform.feedback.domain.model.valueObjects.QuestionType;

import java.util.Objects;

/**
 * Command to create a new QuestionSurvey
 */
public record CreateQuestionSurveyCommand(String textQuestion, QuestionType questionType) {

    /**
     * Constructor with validation
     */
    public CreateQuestionSurveyCommand {
        Objects.requireNonNull(textQuestion, "[CreateQuestionSurveyCommand] textQuestion must not be null");
        Objects.requireNonNull(questionType, "[CreateQuestionSurveyCommand] questionType must not be null");
    }
}
