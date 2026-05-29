package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.FileType;

/**
 * Request object for creating a new Attachment.
 */
public record CreateAttachmentRequest(
        @NotNull
        @JsonProperty("messageId")
        Long messageId,
        @NotNull
        @NotBlank
        String name,
        @NotNull
        @NotBlank
        String url,
        @NotNull
        @NotBlank
        @JsonProperty("fileSize")
        String fileSize,
        @NotNull
        @JsonProperty("fileType")
        FileType fileType
) {}
