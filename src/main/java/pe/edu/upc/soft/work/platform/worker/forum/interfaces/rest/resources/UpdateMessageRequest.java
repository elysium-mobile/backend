package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request object for updating an existing Message.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UpdateMessageRequest(
        @NotNull
        @NotBlank
        Long userAccountId,
        @NotNull
        @NotBlank
        String contentMessage,
        @NotNull
        @NotBlank
        Long threadId
) {}
