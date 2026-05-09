package pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities;

import jakarta.persistence.Entity;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateAttachmentCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateAttachmentCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.FileType;

/**
 * Attachment aggregate root entity.
 */
@Entity
public class Attachment extends AuditableAbstractAggregateRoot<Attachment> {

    @Getter
    private Long messageId;
    @Getter
    private String name;
    @Getter
    private String url;
    @Getter
    private String fileSize;
    @Getter
    private FileType fileType;

    /**
     * Default constructor for JPA.
     */
    public Attachment() {}

    /**
     * Constructor to create a Attachment from a CreateAttachmentCommand.
     * @param command the command containing attachment details
     */
    public Attachment(CreateAttachmentCommand command) {
        this.messageId = command.messageId();
        this.name = command.name();
        this.url = command.url();
        this.fileSize = command.fileSize();
        this.fileType = command.fileType();
    }

    /**
     * Updates the Attachment with details from an UpdateAttachmentCommand.
     * @param command the command containing updated attachment details
     */
    public void updateAttachment(UpdateAttachmentCommand command) {
        this.messageId = command.messageId();
        this.name = command.name();
        this.url = command.url();
        this.fileSize = command.fileSize();
        this.fileType = command.fileType();
    }
}
