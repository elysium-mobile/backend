package pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.FileType;

import java.util.Objects;

/**
 * Command to update an existing Attachment
 */
public record UpdateAssetCommand(Long attachmentId, Long messageId, String name, String url, String fileSize) {

    /**
     * Constructor with validation
     */
    public UpdateAssetCommand {
        Objects.requireNonNull(attachmentId, "[UpdateAttachmentCommand] attachmentId must not be null");
    }
}
