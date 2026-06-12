package pe.edu.upc.soft.work.platform.worker.forum.domain.services;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Thread;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.*;

import java.util.Optional;

/**
 * Service interface for handling Thread-related commands.
 */
public interface ThreadCommandService {

    /**
     * Handles the creation of a new Thread.
     */
    Long handle(CreateThreadCommand command);

    /**
     * Handles the update of an existing Thread.
     */
    Optional<Thread> handle(UpdateThreadCommand command);

    /**
     * Handles the deletion of an existing Thread.
     */
    void handle(DeleteThreadCommand command);

    void handle(AddMessageToThreadCommand command);

    Integer handle(IncrementThreadMessageCountCommand command);

    Integer handle(DecrementThreadMessageCountCommand command);
}
