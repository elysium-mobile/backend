package pe.edu.upc.soft.work.platform.profile.performance.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.aggregates.CommentEmployee;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.CreateCommentEmployeeCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.UpdateCommentEmployeeCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.DeleteCommentEmployeeCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.services.CommentEmployeeCommandService;
import pe.edu.upc.soft.work.platform.profile.performance.infrastructure.persistence.jpa.repositories.CommentEmployeeRepository;

import java.util.Optional;

/**
 * Service implementation for handling CommentEmployee commands, including creation, update, and deletion of CommentEmployee entities.
 */
@Service
public class CommentEmployeeCommandServiceImpl implements CommentEmployeeCommandService {
    private final CommentEmployeeRepository commentemployeeRepository;

    /**
     * Constructor for CommentEmployeeCommandServiceImpl
     * @param commentemployeeRepository the repository for CommentEmployee persistence
     */
    public CommentEmployeeCommandServiceImpl(CommentEmployeeRepository commentemployeeRepository) {
        this.commentemployeeRepository = commentemployeeRepository;
    }

    /**
     * Handles the creation of a CommentEmployee entity based on the provided command.
     * @param command the command to create an CommentEmployee
     * @return the generated ID of the new CommentEmployee
     */
    @Override
    public Long handle(CreateCommentEmployeeCommand command) {
        var commentemployee = new CommentEmployee(command);
        try {
            commentemployeeRepository.save(commentemployee);
        } catch (Exception e) {
            throw new RuntimeException("Error creating CommentEmployee: " + e.getMessage(), e);
        }
        return commentemployee.getId();
    }

    /**
     * Handle the update of an existing CommentEmployee
     * @param command the command to update a CommentEmployee
     * @return the updated CommentEmployee as an Optional
     */
    @Override
    public Optional<CommentEmployee> handle(UpdateCommentEmployeeCommand command) {
        var commentemployeeId = command.commentEmployeeId();
        if (!this.commentemployeeRepository.existsById(commentemployeeId)) {
            throw new RuntimeException("CommentEmployee with ID " + commentemployeeId + " does not exist.");
        }

        var commentemployeeToUpdate = this.commentemployeeRepository.findById(commentemployeeId).get();
        commentemployeeToUpdate.updateCommentEmployee(command);
        try {
            var updatedCommentEmployee = this.commentemployeeRepository.save(commentemployeeToUpdate);
            return Optional.of(updatedCommentEmployee);
        } catch (Exception e) {
            throw new RuntimeException("Error updating CommentEmployee: " + e.getMessage(), e);
        }
    }

    /**
     * Handles the deletion of an CommentEmployee
     * @param command the command to delete a CommentEmployee
     */
    @Override
    public void handle(DeleteCommentEmployeeCommand command) {
        if (!commentemployeeRepository.existsById(command.commentemployeeId())) {
            throw new RuntimeException("CommentEmployee with ID " + command.commentemployeeId() + " does not exist.");
        }
        try {
            commentemployeeRepository.deleteById(command.commentemployeeId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting CommentEmployee: " + e.getMessage(), e);
        }
    }
}
