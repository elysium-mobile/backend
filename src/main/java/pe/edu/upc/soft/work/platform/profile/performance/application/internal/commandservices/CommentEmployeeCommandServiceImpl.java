package pe.edu.upc.soft.work.platform.profile.performance.application.internal.commandservices;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.profile.performance.application.internal.outboundservices.acl.ExternalIamServiceFromProfilePerformance;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.aggregates.CommentEmployee;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.CreateCommentEmployeeCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.UpdateCommentEmployeeCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.DeleteCommentEmployeeCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.events.CommentEmployeeAddedEvent;
import pe.edu.upc.soft.work.platform.profile.performance.domain.services.CommentEmployeeCommandService;
import pe.edu.upc.soft.work.platform.profile.performance.infrastructure.persistence.jpa.repositories.CommentEmployeeRepository;
import pe.edu.upc.soft.work.platform.profile.performance.infrastructure.persistence.jpa.repositories.PerformanceRepository;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;

import java.util.Optional;

/**
 * Service implementation for handling CommentEmployee commands, including creation, update, and deletion of CommentEmployee entities.
 */
@Service
public class CommentEmployeeCommandServiceImpl implements CommentEmployeeCommandService {
    private final CommentEmployeeRepository commentemployeeRepository;
    private final ExternalIamServiceFromProfilePerformance externalIamServiceFromProfilePerformance;
    private final ApplicationEventPublisher eventPublisher;
    private final PerformanceRepository performanceRepository;

    /**
     * Constructor for CommentEmployeeCommandServiceImpl
     * @param commentemployeeRepository the repository for CommentEmployee persistence
     */
    public CommentEmployeeCommandServiceImpl(CommentEmployeeRepository commentemployeeRepository,
                                             ExternalIamServiceFromProfilePerformance externalIamServiceFromProfilePerformance,
                                             ApplicationEventPublisher eventPublisher,
                                             PerformanceRepository performanceRepository) {
        this.commentemployeeRepository = commentemployeeRepository;
        this.externalIamServiceFromProfilePerformance = externalIamServiceFromProfilePerformance;
        this.eventPublisher = eventPublisher;
        this.performanceRepository = performanceRepository;
    }

    /**
     * Handles the creation of a CommentEmployee entity based on the provided command.
     * @param command the command to create an CommentEmployee
     * @return the generated ID of the new CommentEmployee
     */
    @Override
    public Long handle(CreateCommentEmployeeCommand command) {
        if (!this.externalIamServiceFromProfilePerformance.existsRRHHProfileById(command.rrhhProfileId().rrhhProfileId())){
            throw new NotFoundArgumentException(String.format("[CommentEmployeeCommandServiceImpl] RRHH Profile ID: %s not found in the external IAM service",
                            command.rrhhProfileId().rrhhProfileId()));
        }
        if (!performanceRepository.existsById(command.performanceId())){
            throw new NotFoundArgumentException(String.format("[CommentEmployeeCommandServiceImpl] Performance ID: %s not found",
                    command.performanceId()));
        }

        var commentemployee = new CommentEmployee(command);
        eventPublisher.publishEvent(new CommentEmployeeAddedEvent(this, commentemployee.getId(),null));
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
        if (!performanceRepository.existsById(command.performanceId())){
            throw new NotFoundArgumentException(String.format("[CommentEmployeeCommandServiceImpl] Performance ID: %s not found",
                    command.performanceId()));
        }
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
