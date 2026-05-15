package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.edu.upc.soft.work.platform.feedback.domain.model.valueObjects.TargetType;

import java.util.Date;

/**
 * Request object for creating a new Survey.
 */
public record CreateSurveyRequest(
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
