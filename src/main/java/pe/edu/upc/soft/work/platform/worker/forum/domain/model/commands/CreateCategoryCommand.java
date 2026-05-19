package pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands;

import java.util.Objects;
import java.util.Date;

/**
 * Command to create a new Category
 */
public record CreateCategoryCommand(String title, String description) {

    /**
     * Constructor with validation
     */
    public CreateCategoryCommand {
        Objects.requireNonNull(title, "[CreateCategoryCommand] title must not be null");
        Objects.requireNonNull(description, "[CreateCategoryCommand] description must not be null");
    }
}
