package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateQuestionSurveyRequest(
        @NotNull
        @NotBlank
        @JsonProperty("textQuestion")
        String textQuestion,
        @NotNull
        @NotBlank
        @JsonProperty("questionType")
        String questionType,
        @NotNull
        @NotBlank
        @JsonProperty("surveyId")
        Long surveyId
) {
}
