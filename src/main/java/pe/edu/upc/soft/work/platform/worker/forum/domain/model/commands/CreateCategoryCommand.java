package pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Thread;

import java.util.List;
import java.util.Objects;
import java.util.Date;

/**
 * Command to create a new Category
 */
public record CreateCategoryCommand(String title, String description,Long forumId,
                                    List<Thread> threads) {

    /**
     * Constructor with validation
     */
    public CreateCategoryCommand {
        Objects.requireNonNull(title, "[CreateCategoryCommand] title must not be null");
        Objects.requireNonNull(description, "[CreateCategoryCommand] description must not be null");
    }
}
