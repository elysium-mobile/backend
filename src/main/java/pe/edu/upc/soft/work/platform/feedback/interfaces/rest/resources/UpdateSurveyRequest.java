package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

/**
 * Request object for updating an existing Survey.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UpdateSurveyRequest(
        @NotNull
        @NotBlank
        String title,
        @NotNull
        @NotBlank
        String description,
        @NotNull
        @NotBlank
        String targetType,
        @NotNull
        Date expirationTime
) {}
