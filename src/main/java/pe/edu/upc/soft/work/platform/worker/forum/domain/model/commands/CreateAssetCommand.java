package pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.FileType;

import java.util.Objects;

/**
 * Command to create a new Attachment
 */
public record CreateAssetCommand(Long messageId, String name, String url, String fileSize, FileType fileType) {

    public CreateAssetCommand(Long messageId, String name, FileType fileType) {
        this(messageId, name, null, null, fileType);
    }

    /**
     * Constructor with validation
     */
    public CreateAssetCommand {
        Objects.requireNonNull(messageId, "[CreateAttachmentCommand] messageId must not be null");
        Objects.requireNonNull(name, "[CreateAttachmentCommand] name must not be null");
        Objects.requireNonNull(fileType, "[CreateAttachmentCommand] fileType must not be null");
    }
}
