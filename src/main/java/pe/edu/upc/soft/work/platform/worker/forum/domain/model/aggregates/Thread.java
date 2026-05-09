package pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates;

import jakarta.persistence.Entity;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateThreadCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateThreadCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import java.util.Date;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.AreaCompanyId;

/**
 * Thread aggregate root entity.
 */
@Entity
public class Thread extends AuditableAbstractAggregateRoot<Thread> {

    @Getter
    private String title;
    @Getter
    private AreaCompanyId areaCompanyId;
    @Getter
    private Date lastMessage;

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
