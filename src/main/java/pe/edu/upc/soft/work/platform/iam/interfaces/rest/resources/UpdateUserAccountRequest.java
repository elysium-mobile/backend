package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UpdateUserAccountRequest(

        @NotNull
        Long userId,

        @NotNull
        @NotBlank
        String email,

        @NotNull
        @NotBlank
        String password,

        @NotNull
        @NotBlank
        String anonymousName,

        @NotNull
        Long membershipId,

        @NotNull
        Long companyId
) {
}
