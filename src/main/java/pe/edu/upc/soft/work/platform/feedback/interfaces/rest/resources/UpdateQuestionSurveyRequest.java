package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateQuestionSurveyRequest(

        @NotNull
        @NotBlank
        String textQuestion,

        @NotNull
        @NotBlank
        String questionType
) {
}
