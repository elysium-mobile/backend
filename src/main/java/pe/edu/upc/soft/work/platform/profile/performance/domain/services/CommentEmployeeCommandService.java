package pe.edu.upc.soft.work.platform.profile.performance.domain.services;

import pe.edu.upc.soft.work.platform.profile.performance.domain.model.aggregates.CommentEmployee;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.CreateCommentEmployeeCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.UpdateCommentEmployeeCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.DeleteCommentEmployeeCommand;

import java.util.Optional;

/**
 * Service interface for handling CommentEmployee-related commands.
 */
public interface CommentEmployeeCommandService {

    /**
     * Handles the creation of a new CommentEmployee.
     */
    Long handle(CreateCommentEmployeeCommand command);

    /**
     * Handles the update of an existing CommentEmployee.
     */
    Optional<CommentEmployee> handle(UpdateCommentEmployeeCommand command);

    /**
     * Handles the deletion of an existing CommentEmployee.
     */
    void handle(DeleteCommentEmployeeCommand command);
}
