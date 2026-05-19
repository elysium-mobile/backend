package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record UpdateUserRequest(
        @NotNull
        @NotBlank
        @JsonProperty("name")
        String name,
        @NotNull
        @NotBlank
        @JsonProperty("lastName")
        String lastName,
        @NotNull
        @NotBlank
        @JsonProperty("phoneNumber")
        String phoneNumber,
        @NotNull
        @NotBlank
        @JsonProperty("dni")
        @Min(0)
        String dni
) {

}
