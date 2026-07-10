package pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateThreadCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateThreadCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.AreaCompanyId;

/**
 * Thread aggregate root entity.
 */
@Entity
@Table(name = "threads")
public class Thread extends AuditableAbstractAggregateRoot<Thread> {

    @Getter
    @Column(name = "title", nullable = false)
    private String title;
    @Getter
    @Embedded
    @AttributeOverrides(
            @AttributeOverride(name = "areaCompanyId", column = @Column(name = "area_company_id", nullable = false)
    ))
    @JsonProperty("area_company_id")
    private AreaCompanyId areaCompanyId;
    @Getter
    @Column(name = "last_message", nullable = false)
    private Date lastMessage;

    @Getter
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    @Getter
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Getter
    @Column(name = "message_count", nullable = false)
    private Integer messageCount;

    /**
     * Default constructor for JPA.
     */
    public Thread() {}

    /**
     * Constructor to create a Thread from a CreateThreadCommand.
     * @param command the command containing thread details
     */
    public Thread(CreateThreadCommand command) {
        this.title = command.title();
        this.areaCompanyId = command.areaCompanyId();
        this.lastMessage = command.lastMessage();
        this.messages=command.messages();
        this.categoryId=command.categoryId();
        this.messageCount = command.messageCount();
    }

    /**
     * Updates the Thread with details from an UpdateThreadCommand.
     * @param command the command containing updated thread details
     */
    public void updateThread(UpdateThreadCommand command) {
        this.title = command.title();
        this.areaCompanyId = command.areaCompanyId();
        this.lastMessage = command.lastMessage();
        this.categoryId=command.categoryId();
        this.messageCount=command.messageCount();
    }

    public void incrementMessageCount(){
        this.messageCount+=1;
    }

    public void decrementMessageCount(){
        if (this.messageCount>0){
            this.messageCount -=1;
        }
    }

    public void addMessage(Message message){
        if (this.messages == null){
            this.messages = new ArrayList<>();
        }
        boolean alreadyExists = this.messages.stream()
                .anyMatch(m -> m.getId().equals(message.getId()));
        if (!alreadyExists) {
            this.messages.add(message);
        }
    }

    public void removeMessage(Long messageId) {
        if (this.messages == null) {
            throw new IllegalArgumentException("Message with ID " + messageId + " not found in this thread.");
        }
        boolean removed = this.messages.removeIf(m -> m.getId().equals(messageId));
        if (!removed) {
            throw new IllegalArgumentException("Message with ID " + messageId + " not found in this thread.");
        }
    }
}
