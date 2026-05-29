package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record UpdateSurveyResponseRequest(
        @NotNull
        @NotBlank
        Long surveyId,
        @NotNull
        @NotBlank
        @JsonProperty("employeeProfileId")
        Long employeeProfileId,
        @NotNull
        @NotBlank
        @JsonProperty("submittedAt")
        Date submittedAt
) {
}
