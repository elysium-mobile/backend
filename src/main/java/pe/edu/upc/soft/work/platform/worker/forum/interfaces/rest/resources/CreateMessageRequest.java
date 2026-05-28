package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request object for creating a new Message.
 */
public record CreateMessageRequest(
        @NotNull
        @NotBlank
        Long userAccountId,
        @NotNull
        @NotBlank
        String contentMessage
) {}
