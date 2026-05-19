package pe.edu.upc.soft.work.platform.worker.forum.domain.services;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Forum;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateForumCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateForumCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteForumCommand;

import java.util.Optional;

/**
 * Service interface for handling Forum-related commands.
 */
public interface ForumCommandService {

    /**
     * Handles the creation of a new Forum.
     */
    Long handle(CreateForumCommand command);

    /**
     * Handles the update of an existing Forum.
     */
    Optional<Forum> handle(UpdateForumCommand command);

    /**
     * Handles the deletion of an existing Forum.
     */
    void handle(DeleteForumCommand command);
}
