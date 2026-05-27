package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request object for creating a new QuestionSurvey.
 */
public record CreateQuestionSurveyRequest(
        @NotNull
        @NotBlank
        String textQuestion,
        @NotNull
        @NotBlank
        String questionType
) {
}
