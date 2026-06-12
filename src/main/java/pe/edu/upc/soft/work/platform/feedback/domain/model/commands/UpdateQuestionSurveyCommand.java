package pe.edu.upc.soft.work.platform.feedback.domain.model.commands;

import pe.edu.upc.soft.work.platform.feedback.domain.model.valueObjects.QuestionType;

import java.util.Objects;

/**
 * Command to update an existing QuestionSurvey
 */
public record UpdateQuestionSurveyCommand(Long questionSurveyId, String textQuestion, QuestionType questionType, Long surveyId) {

    /**
     * Constructor with validation
     */
    public UpdateQuestionSurveyCommand {
        Objects.requireNonNull(questionSurveyId, "[UpdateQuestionSurveyCommand] questionsurveyId must not be null");
    }
}
