package pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.FileType;

import java.util.Objects;
import java.util.Date;

/**
 * Command to create a new Attachment
 */
public record CreateAttachmentCommand(Long messageId, String name, String url, String fileSize, FileType fileType) {

    /**
     * Constructor with validation
     */
    public CreateAttachmentCommand {
        Objects.requireNonNull(messageId, "[CreateAttachmentCommand] messageId must not be null");
        Objects.requireNonNull(name, "[CreateAttachmentCommand] name must not be null");
        Objects.requireNonNull(url, "[CreateAttachmentCommand] url must not be null");
        Objects.requireNonNull(fileSize, "[CreateAttachmentCommand] fileSize must not be null");
        Objects.requireNonNull(fileType, "[CreateAttachmentCommand] fileType must not be null");
    }
}
