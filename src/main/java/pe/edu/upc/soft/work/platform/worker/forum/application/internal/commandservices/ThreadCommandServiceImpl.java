package pe.edu.upc.soft.work.platform.worker.forum.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;
import pe.edu.upc.soft.work.platform.worker.forum.application.internal.outboundservices.acl.ExternalDashboardServiceFromWorkerForum;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Thread;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateThreadCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateThreadCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteThreadCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.ThreadCommandService;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.ThreadRepository;

import java.util.Optional;

/**
 * Service implementation for handling Thread
 */
@Service
public class ThreadCommandServiceImpl implements ThreadCommandService {
    private final ThreadRepository threadRepository;

    private final ExternalDashboardServiceFromWorkerForum externalDashboardServiceFromWorkerForum;

    /**
     * Constructor for ThreadCommandServiceImpl.
     * @param threadRepository the repository for Thread persistence
     */
    public ThreadCommandServiceImpl(ThreadRepository threadRepository,
                                    ExternalDashboardServiceFromWorkerForum externalDashboardServiceFromWorkerForum) {
        this.threadRepository = threadRepository;
        this.externalDashboardServiceFromWorkerForum = externalDashboardServiceFromWorkerForum;
    }

    /**
     * Handles the creation of a Thread
     * @param command the command to create an Thread
     * @return the generated ID of the new Thread
     */
    @Override
    public Long handle(CreateThreadCommand command) {

        if (!this.externalDashboardServiceFromWorkerForum.existsCompanyById(command.areaCompanyId().areaCompanyId())){
            throw new NotFoundArgumentException(
                    String.format("[ThreadCommandServiceImpl] Company ID: %s not found in the external Dashboard Context",
                            command.areaCompanyId().areaCompanyId())
            );
        }
        var thread = new Thread(command);
        try {
            threadRepository.save(thread);
        } catch (Exception e) {
            throw new RuntimeException("Error creating Thread: " + e.getMessage(), e);
        }
        return thread.getId();
    }

    /**
     * Handles the update of an existing Thread
     * @param command the command to update an Thread
     * @return the updated Thread as an Optional
     */
    @Override
    public Optional<Thread> handle(UpdateThreadCommand command) {
        var threadId = command.threadId();
        if (!this.threadRepository.existsById(threadId)) {
            throw new RuntimeException("Thread with ID " + threadId + " does not exist.");
        }

        var threadToUpdate = this.threadRepository.findById(threadId).get();
        threadToUpdate.updateThread(command);
        try {
            var updatedThread = this.threadRepository.save(threadToUpdate);
            return Optional.of(updatedThread);
        } catch (Exception e) {
            throw new RuntimeException("Error updating Thread: " + e.getMessage(), e);
        }
    }

    /**
     * Handles the deletion of an Thread
     * @param command the command to delete an Thread
     */
    @Override
    public void handle(DeleteThreadCommand command) {
        if (!threadRepository.existsById(command.threadId())) {
            throw new RuntimeException("Thread with ID " + command.threadId() + " does not exist.");
        }
        try {
            threadRepository.deleteById(command.threadId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting Thread: " + e.getMessage(), e);
        }
    }
}
