package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.edu.upc.soft.work.platform.feedback.domain.model.valueObjects.TargetType;

import java.util.Date;

/**
 * Request object for updating an existing Survey.
 */
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
        @NotBlank
        Date expirationTime
) {}
