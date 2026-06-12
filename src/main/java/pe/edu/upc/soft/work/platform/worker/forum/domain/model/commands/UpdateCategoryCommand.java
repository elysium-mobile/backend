package pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands;

import java.util.Objects;
import java.util.Date;

/**
 * Command to update an existing Category
 */
public record UpdateCategoryCommand(Long categoryId, String title, String description, Long forumId) {

    /**
     * Constructor with validation
     */
    public UpdateCategoryCommand {
        Objects.requireNonNull(categoryId, "[UpdateCategoryCommand] categoryId must not be null");
    }
}
