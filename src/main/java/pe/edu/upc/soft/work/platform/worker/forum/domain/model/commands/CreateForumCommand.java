package pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Category;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.CompanyId;

import java.util.List;
import java.util.Objects;
import java.util.Date;

/**
 * Command to create a new Forum
 */
public record CreateForumCommand(String title, String description, CompanyId companyId,
                                 List<Category> categories) {

    /**
     * Constructor with validation
     */
    public CreateForumCommand {
        Objects.requireNonNull(title, "[CreateForumCommand] title must not be null");
        Objects.requireNonNull(description, "[CreateForumCommand] description must not be null");
        Objects.requireNonNull(companyId, "[CreateForumCommand] companyId must not be null");
    }
}
