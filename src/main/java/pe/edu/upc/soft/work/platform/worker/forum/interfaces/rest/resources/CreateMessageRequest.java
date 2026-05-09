package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.UserAccountId;

import java.util.Date;

/**
 * Request object for creating a new Message.
 */
public record CreateMessageRequest(
        @NotNull
        @NotBlank
        UserAccountId userAccountId,
        @NotNull
        @NotBlank
        String contentMessage
) {}
