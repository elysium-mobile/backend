package pe.edu.upc.soft.work.platform.profile.performance.domain.model.aggregates;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.CreateCommentEmployeeCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.UpdateCommentEmployeeCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.valueobjects.RRHHProfileId;

/**
 * CommentEmployee aggregate root entity.
 */
@Entity
@Table(name = "comment_employees")
public class CommentEmployee extends AuditableAbstractAggregateRoot<CommentEmployee> {

    @Getter
    @Column(name = "title", nullable = false, length = 100)
    private String title;
    @Getter
    @Column(name = "content", nullable = false, length = 500)
    private String content;
    @Getter
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "rrhhProfileId", column = @Column(name = "rrhh_profile_id", nullable = false))
    })
    @JsonProperty("id_rrhh_profile")
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
