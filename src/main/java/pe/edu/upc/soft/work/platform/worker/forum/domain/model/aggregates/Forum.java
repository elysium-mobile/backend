package pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateForumCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateForumCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.CompanyId;


/**
 * Forum aggregate root entity.
 */
@Entity
@Table(name = "forums")
public class Forum extends AuditableAbstractAggregateRoot<Forum> {

    @Getter
    @Column(name = "title", nullable = false, length = 100)
    private String title;
    @Getter
    @Column(name = "description", nullable = false, length = 500)
    private String description;
    @Getter
    @Embedded
    @AttributeOverrides(
            @AttributeOverride(name = "companyId", column = @Column(name = "company_id", nullable = false, length = 10))
    )
    @JsonProperty("id_company")
    private CompanyId companyId;

    /**
     * Default constructor for JPA.
     */
    public Forum() {}

    /**
     * Constructor to create a Forum from a CreateForumCommand.
     * @param command the command containing forum details
     */
    public Forum(CreateForumCommand command) {
        this.title = command.title();
        this.description = command.description();
        this.companyId = command.companyId();
    }

    /**
     * Updates the Forum with details from an UpdateForumCommand.
     * @param command the command containing updated forum details
     */
    public void updateForum(UpdateForumCommand command) {
        this.title = command.title();
        this.description = command.description();
        this.companyId = command.companyId();
    }
}
