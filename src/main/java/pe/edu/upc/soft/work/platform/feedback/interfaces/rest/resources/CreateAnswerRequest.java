package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request object for creating a new Answer Request.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateAnswerRequest(

        @NotNull
        Long value,
        @NotNull
        Integer scoreAnswer

) {
}
