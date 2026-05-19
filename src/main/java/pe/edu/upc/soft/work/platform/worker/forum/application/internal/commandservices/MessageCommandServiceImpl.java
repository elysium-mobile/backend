package pe.edu.upc.soft.work.platform.worker.forum.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Message;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateMessageCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateMessageCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteMessageCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.MessageCommandService;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.MessageRepository;

import java.util.Optional;

/**
 * Service implementation for handling Message commands
 */
@Service
public class MessageCommandServiceImpl implements MessageCommandService {
    private final MessageRepository messageRepository;

    /**
     * Constructor for MessageCommandServiceImpl.
     * @param messageRepository the repository for Message persistence
     */
    public MessageCommandServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * Handles the creation of an Message
     * @param command the command to create an Message
     * @return the generated ID of the new Message
     */
    @Override
    public Long handle(CreateMessageCommand command) {
        var message = new Message(command);
        try {
            messageRepository.save(message);
        } catch (Exception e) {
            throw new RuntimeException("Error creating Message: " + e.getMessage(), e);
        }
        return message.getId();
    }

    /**
     * Handles the update of an existing Message
     * @param command the command to update a Message
     * @return the updated Message as an Optional
     */
    @Override
    public Optional<Message> handle(UpdateMessageCommand command) {
        var messageId = command.messageId();
        if (!this.messageRepository.existsById(messageId)) {
            throw new RuntimeException("Message with ID " + messageId + " does not exist.");
        }

        var messageToUpdate = this.messageRepository.findById(messageId).get();
        messageToUpdate.updateMessage(command);
        try {
            var updatedMessage = this.messageRepository.save(messageToUpdate);
            return Optional.of(updatedMessage);
        } catch (Exception e) {
            throw new RuntimeException("Error updating Message: " + e.getMessage(), e);
        }
    }

    /**
     * Handles the deletion of an Message
     * @param command the command to delete an Message
     */
    @Override
    public void handle(DeleteMessageCommand command) {
        if (!messageRepository.existsById(command.messageId())) {
            throw new RuntimeException("Message with ID " + command.messageId() + " does not exist.");
        }
        try {
            messageRepository.deleteById(command.messageId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting Message: " + e.getMessage(), e);
        }
    }
}
