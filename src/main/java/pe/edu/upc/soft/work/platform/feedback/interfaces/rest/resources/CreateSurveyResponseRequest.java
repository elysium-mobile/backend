package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record CreateSurveyResponseRequest(
        @NotNull
        @NotBlank
        Long surveyId,
        @NotNull
        @NotBlank
        Long employeeProfileId,
        @NotNull
        @NotBlank
        Date submittedAt
) {
}
