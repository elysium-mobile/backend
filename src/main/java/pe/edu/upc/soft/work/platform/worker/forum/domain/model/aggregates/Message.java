package pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateMessageCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateMessageCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Asset;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.UserAccountId;

import java.util.List;


/**
 * Message aggregate root entity.
 */
@Entity
@Table(name = "messages")
public class Message extends AuditableAbstractAggregateRoot<Message> {

    @Getter
    @Embedded
    @AttributeOverrides(
            @AttributeOverride(name = "userAccountId", column = @Column(name = "user_account_id", nullable = false, length = 10)
    ))
    @JsonProperty("id_user_account")
    private UserAccountId userAccountId;
    @Getter
    @Column(name = "content_message", nullable = false, length = 500)
    private String contentMessage;

    @Getter
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "attachments", nullable = true)
    private List<Asset> assets;

    @Getter
    @Column(name = "thread_id", nullable = false)
    private Long threadId;

    /**
     * Default constructor for JPA.
     */
    public Message() {}

    /**
     * Constructor to create a Message from a CreateMessageCommand.
     * @param command the command containing message details
     */
    public Message(CreateMessageCommand command) {
        this.userAccountId = command.userAccountId();
        this.contentMessage = command.contentMessage();
        this.assets =command.assets();
        this.threadId = command.threadId();
    }

    /**
     * Updates the Message with details from an UpdateMessageCommand.
     * @param command the command containing updated message details
     */
    public void updateMessage(UpdateMessageCommand command) {
        this.userAccountId = command.userAccountId();
        this.contentMessage = command.contentMessage();
        this.threadId = command.threadId();
    }

    public void addAttachment(Asset asset){
        if (this.assets == null){
            this.assets = new java.util.ArrayList<>();
        }
        boolean alreadyExists = this.assets.stream()
                .anyMatch(existingAttachment -> existingAttachment.getId().equals(asset.getId()));
        if (!alreadyExists) {
            this.assets.add(asset);
        }
    }

    public void removeAttachment(Long assetId) {
        if (this.assets == null) {
            throw new IllegalArgumentException("Asset with ID " + assetId + " not found in this message.");
        }
        boolean removed = this.assets.removeIf(a -> a.getId().equals(assetId));
        if (!removed) {
            throw new IllegalArgumentException("Asset with ID " + assetId + " not found in this message.");
        }
    }

    public boolean hasViewableAssets() {
        return assets.stream().anyMatch(Asset::isViewable);
    }

    public boolean hasReadableAssets() {
        return assets.stream().anyMatch(Asset::isReadable);
    }



}
