package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request object for creating a new Answer Request.
 */
public record CreateAnswerRequest(

        @NotNull
        @NotBlank
        Long value,
        @NotNull
        @NotBlank
        Integer scoreAnswer

) {
}
