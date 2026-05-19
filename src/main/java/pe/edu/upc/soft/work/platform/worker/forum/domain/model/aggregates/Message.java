package pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateMessageCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateMessageCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.UserAccountId;


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
    }

    /**
     * Updates the Message with details from an UpdateMessageCommand.
     * @param command the command containing updated message details
     */
    public void updateMessage(UpdateMessageCommand command) {
        this.userAccountId = command.userAccountId();
        this.contentMessage = command.contentMessage();
    }
}
