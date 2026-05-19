package pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.FileType;

import java.util.Objects;
import java.util.Date;

/**
 * Command to update an existing Attachment
 */
public record UpdateAttachmentCommand(Long attachmentId, Long messageId, String name, String url, String fileSize, FileType fileType) {

    /**
     * Constructor with validation
     */
    public UpdateAttachmentCommand {
        Objects.requireNonNull(attachmentId, "[UpdateAttachmentCommand] attachmentId must not be null");
    }
}
