package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.FileType;

/**
 * Request object for updating an existing Attachment.
 */
public record UpdateAttachmentRequest(
        @NotNull
        Long messageId,
        @NotNull
        @NotBlank
        String name,
        @NotNull
        @NotBlank
        String url,
        @NotNull
        @NotBlank
        String fileSize,
        @NotNull
        FileType fileType
) {}
