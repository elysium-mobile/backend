package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request object for updating an existing Message.
 */
public record UpdateMessageRequest(
        @NotNull
        @NotBlank
        Long userAccountId,
        @NotNull
        @NotBlank
        String contentMessage
) {}
