package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateAnswerRequest(
        @NotNull
        @NotBlank
        Long value,
        @NotNull
        @NotBlank
        @JsonProperty("scoreAnswer")
        Integer scoreAnswer
)
{
}
