package pe.edu.upc.soft.work.platform.worker.forum.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Thread;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateThreadCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateThreadCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteThreadCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.ThreadCommandService;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.ThreadRepository;

import java.util.Optional;

@Service
public class ThreadCommandServiceImpl implements ThreadCommandService {
    private final ThreadRepository threadRepository;

    public ThreadCommandServiceImpl(ThreadRepository threadRepository) {
        this.threadRepository = threadRepository;
    }

    @Override
    public Long handle(CreateThreadCommand command) {
        var thread = new Thread(command);
        try {
            threadRepository.save(thread);
        } catch (Exception e) {
            throw new RuntimeException("Error creating Thread: " + e.getMessage(), e);
        }
        return thread.getId();
    }

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
