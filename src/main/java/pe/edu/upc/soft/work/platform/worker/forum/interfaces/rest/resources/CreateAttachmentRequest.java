package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.FileType;

/**
 * Request object for creating a new Attachment.
 */
public record CreateAttachmentRequest(
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
