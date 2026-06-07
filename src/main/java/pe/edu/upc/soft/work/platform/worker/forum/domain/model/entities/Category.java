package pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Thread;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateCategoryCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateCategoryCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.util.List;


/**
 * Category aggregate root entity.
 */
@Entity
@Table(name = "categories")
public class Category extends AuditableAbstractAggregateRoot<Category> {

    @Getter
    @Column(name = "title", nullable = false, length = 100)
    private String title;
    @Getter
    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Getter
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "threads", nullable = true)
    private List<Thread> threads;

    @Getter
    @Column(name = "forum_id", nullable = false)
    private Long forumId;

    /**
     * Default constructor for JPA.
     */
    public Category() {}

    /**
     * Constructor to create a Category from a CreateCategoryCommand.
     * @param command the command containing category details
     */
    public Category(CreateCategoryCommand command) {
        this.title = command.title();
        this.description = command.description();
        this.threads = command.threads();
    }

    /**
     * Updates the Category with details from an UpdateCategoryCommand.
     * @param command the command containing updated category details
     */
    public void updateCategory(UpdateCategoryCommand command) {
        this.title = command.title();
        this.description = command.description();
    }
}
