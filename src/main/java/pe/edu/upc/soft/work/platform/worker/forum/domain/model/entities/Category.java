package pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateCategoryCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateCategoryCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;


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
