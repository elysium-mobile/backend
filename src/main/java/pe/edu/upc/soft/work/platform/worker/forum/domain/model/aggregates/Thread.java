package pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateThreadCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateThreadCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
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
    @Column(name = "messages", nullable = true)
    private List<Message> messages;

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
    }

    /**
     * Updates the Thread with details from an UpdateThreadCommand.
     * @param command the command containing updated thread details
     */
    public void updateThread(UpdateThreadCommand command) {
        this.title = command.title();
        this.areaCompanyId = command.areaCompanyId();
        this.lastMessage = command.lastMessage();
    }
}
