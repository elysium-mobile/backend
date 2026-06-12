package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request object for updating an existing Message.
 */
public record UpdateMessageRequest(
        @NotNull
        @NotBlank
        @JsonProperty("userAccountId")
        Long userAccountId,
        @NotNull
        @NotBlank
        @JsonProperty("contentMessage")
        String contentMessage,
        @NotNull
        @NotBlank
        @JsonProperty("threadId")
        Long threadId
) {}
