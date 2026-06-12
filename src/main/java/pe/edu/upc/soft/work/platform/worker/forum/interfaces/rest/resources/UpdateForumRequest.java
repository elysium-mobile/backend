package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request object for updating an existing Forum.
 */
public record UpdateForumRequest(
        @NotNull
        @NotBlank
        String title,
        @NotNull
        @NotBlank
        String description,
        @NotNull
        @NotBlank
        @JsonProperty("companyId")
        Long companyId
) {}
