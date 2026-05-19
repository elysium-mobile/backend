package pe.edu.upc.soft.work.platform.worker.forum.domain.services;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Message;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateMessageCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateMessageCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteMessageCommand;

import java.util.Optional;

/**
 * Service interface for handling Message-related commands.
 */
public interface MessageCommandService {

    /**
     * Handles the creation of a new Message.
     */
    Long handle(CreateMessageCommand command);

    /**
     * Handles the update of an existing Message.
     */
    Optional<Message> handle(UpdateMessageCommand command);

    /**
     * Handles the deletion of an existing Message.
     */
    void handle(DeleteMessageCommand command);
}
