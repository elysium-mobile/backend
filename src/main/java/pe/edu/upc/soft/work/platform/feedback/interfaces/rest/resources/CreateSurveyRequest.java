package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
        @JsonProperty("targetType")
        String targetType,
        @NotNull
        @NotBlank
        @JsonProperty("expirationType")
        Date expirationTime
) {}
