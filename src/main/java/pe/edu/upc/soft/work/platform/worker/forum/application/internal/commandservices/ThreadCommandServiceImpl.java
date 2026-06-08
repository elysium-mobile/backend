package pe.edu.upc.soft.work.platform.worker.forum.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;
import pe.edu.upc.soft.work.platform.worker.forum.application.internal.outboundservices.acl.ExternalDashboardServiceFromWorkerForum;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Thread;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.*;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.ThreadCommandService;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.CategoryRepository;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.MessageRepository;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.ThreadRepository;

import java.util.Optional;

/**
 * Service implementation for handling Thread
 */
@Service
public class ThreadCommandServiceImpl implements ThreadCommandService {
    private final ThreadRepository threadRepository;
    private final CategoryRepository categoryRepository;
    private final MessageRepository messageRepository;

    private final ExternalDashboardServiceFromWorkerForum externalDashboardServiceFromWorkerForum;

    /**
     * Constructor for ThreadCommandServiceImpl.
     * @param threadRepository the repository for Thread persistence
     */
    public ThreadCommandServiceImpl(ThreadRepository threadRepository,
                                    ExternalDashboardServiceFromWorkerForum externalDashboardServiceFromWorkerForum,
                                    CategoryRepository categoryRepository,
                                    MessageRepository messageRepository) {
        this.threadRepository = threadRepository;
        this.externalDashboardServiceFromWorkerForum = externalDashboardServiceFromWorkerForum;
        this.categoryRepository = categoryRepository;
        this.messageRepository = messageRepository;
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
        if (!categoryRepository.existsById(command.categoryId())){
            throw new NotFoundArgumentException(
                    String.format("[ThreadCommandServiceImpl] Category ID: %s not found in the database",
                            command.categoryId())
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
        if (!categoryRepository.existsById(command.categoryId())){
            throw new NotFoundArgumentException(
                    String.format("[ThreadCommandServiceImpl] Category ID: %s not found in the database",
                            command.categoryId())
            );
        }
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

    @Override
    public void handle(AddMessageToThreadCommand command) {
        var message = messageRepository.findById(command.messageId())
                .orElseThrow(() -> new NotFoundArgumentException(
                        String.format("[ThreadCommandServiceImpl] Message with ID: %s not found in the database",
                                command.messageId())
                ));
        var thread = threadRepository.findById(command.threadId())
                .orElseThrow(() -> new NotFoundArgumentException(
                        String.format("[ThreadCommandServiceImpl] Thread with ID: %s not found in the database",
                                command.threadId())
                ));
        try{
            thread.addMessage(message);
            threadRepository.save(thread);
        }catch (IllegalStateException ex){
            throw new IllegalArgumentException("Domain error while adding Message to Thread: " + ex.getMessage());
        }catch (Exception e){
            throw new IllegalArgumentException("Error adding Message to Thread: " + e.getMessage(), e);
        }
    }

    @Override
    public Integer handle(IncrementThreadMessageCountCommand command) {
        var thread = this.threadRepository.findById(command.threadId())
                .orElseThrow(() -> new NotFoundArgumentException(
                        String.format("[ThreadCommandServiceImpl] Thread with ID: %s not found in the database",
                                command.threadId())
                ));
        thread.incrementMessageCount();
        try{
            this.threadRepository.save(thread);
            return thread.getMessageCount();
        }catch (Exception e){
            throw new RuntimeException("Error incrementing Thread message count: " + e.getMessage(), e);
        }
    }

    @Override
    public Integer handle(DecrementThreadMessageCountCommand command) {
        var thread = this.threadRepository.findById(command.threadId())
                .orElseThrow(() -> new NotFoundArgumentException(
                        String.format("[ThreadCommandServiceImpl] Thread with ID: %s not found in the database",
                                command.threadId())
                ));
        thread.decrementMessageCount();
        try{
            this.threadRepository.save(thread);
            return thread.getMessageCount();
        }catch (Exception e){
            throw new RuntimeException("Error decrementing Thread message count: " + e.getMessage(), e);
        }
    }

}
