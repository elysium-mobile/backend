package pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateAssetCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateAssetCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.FileType;

/**
 * Attachment aggregate root entity.
 */
@Entity
@Table(name = "assets")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "file_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Asset extends AuditableAbstractAggregateRoot<Asset> {

    @Setter
    @Getter
    @Column(name = "message_id", nullable = false)
    private Long messageId;
    @Getter
    @Column(name = "name", nullable = false)
    private String name;
    @Getter
    @Column(name = "url", nullable = false)
    private String url;
    @Getter
    @Column(name = "file_size", nullable = false)
    private String fileSize;


    /**
     * Default constructor for JPA.
     */
    public Asset() {}

    protected Asset(Long messageId, String name, String url, String fileSize) {
        this.messageId = messageId;
        this.name = name;
        this.url = url;
        this.fileSize = fileSize;
    }

    /**
     * Constructor to create a Attachment from a CreateAttachmentCommand.
     * @param command the command containing attachment details
     */
    public Asset(CreateAssetCommand command) {
        this.messageId = command.messageId();
        this.name = command.name();
        this.url = command.url();
        this.fileSize = command.fileSize();

    }

    /**
     * Updates the Attachment with details from an UpdateAttachmentCommand.
     * @param command the command containing updated attachment details
     */
    public void updateAttachment(UpdateAssetCommand command) {
        this.messageId = command.messageId();
        this.name = command.name();
        this.url = command.url();
        this.fileSize = command.fileSize();

    }

    public void updateAsset(Long messageId, String name, String url, String fileSize) {
        this.messageId = messageId;
        this.name = name;
        this.url = url;
        this.fileSize = fileSize;
    }

    public abstract FileType getFileType();

    public abstract boolean isViewable();

    public abstract boolean isReadable();


}
