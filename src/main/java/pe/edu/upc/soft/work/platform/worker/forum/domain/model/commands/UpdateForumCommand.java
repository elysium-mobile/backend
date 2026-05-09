package pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.CompanyId;

import java.util.Objects;
import java.util.Date;

/**
 * Command to update an existing Forum
 */
public record UpdateForumCommand(Long forumId, String title, String description, CompanyId companyId) {

    /**
     * Constructor with validation
     */
    public UpdateForumCommand {
        Objects.requireNonNull(forumId, "[UpdateForumCommand] forumId must not be null");
    }
}
