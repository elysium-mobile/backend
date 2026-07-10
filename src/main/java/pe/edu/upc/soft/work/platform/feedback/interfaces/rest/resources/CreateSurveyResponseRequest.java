package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateSurveyResponseRequest(
        @NotNull
        Long surveyId,
        @NotNull
        Long employeeProfileId,
        @NotNull
        Date submittedAt,
        @NotNull
        @NotBlank
        String commentary,
        @NotNull
        @NotBlank
        String cause
) {
}
