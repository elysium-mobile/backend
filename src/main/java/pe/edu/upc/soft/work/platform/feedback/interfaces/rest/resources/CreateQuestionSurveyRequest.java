package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request object for creating a new QuestionSurvey.
 */
public record CreateQuestionSurveyRequest(
        @NotNull
        @NotBlank
        @JsonProperty("textQuestion")
        String textQuestion,
        @NotNull
        @NotBlank
        @JsonProperty("questionType")
        String questionType
) {
}
