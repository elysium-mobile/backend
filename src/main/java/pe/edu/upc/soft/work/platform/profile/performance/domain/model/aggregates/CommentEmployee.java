package pe.edu.upc.soft.work.platform.profile.performance.domain.model.aggregates;

import jakarta.persistence.Entity;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.CreateCommentEmployeeCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.UpdateCommentEmployeeCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.valueobjects.RRHHProfileId;

/**
 * CommentEmployee aggregate root entity.
 */
@Entity
public class CommentEmployee extends AuditableAbstractAggregateRoot<CommentEmployee> {

    @Getter
    private String title;
    @Getter
    private String content;
    @Getter
    private RRHHProfileId rrhhProfileId;

    /**
     * Default constructor for JPA.
     */
    public CommentEmployee() {}

    /**
     * Constructor to create a CommentEmployee from a CreateCommentEmployeeCommand.
     * @param command the command containing commentemployee details
     */
    public CommentEmployee(CreateCommentEmployeeCommand command) {
        this.title = command.title();
        this.content = command.content();
        this.rrhhProfileId = command.rrhhProfileId();
    }

    /**
     * Updates the CommentEmployee with details from an UpdateCommentEmployeeCommand.
     * @param command the command containing updated commentemployee details
     */
    public void updateCommentEmployee(UpdateCommentEmployeeCommand command) {
        this.title = command.title();
        this.content = command.content();
        this.rrhhProfileId = command.rrhhProfileId();
    }
}
