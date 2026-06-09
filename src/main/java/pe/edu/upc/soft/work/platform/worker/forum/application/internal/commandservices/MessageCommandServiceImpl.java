package pe.edu.upc.soft.work.platform.worker.forum.application.internal.commandservices;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.payment.service.application.internal.outboundservices.acl.ExternalIamServiceFromPaymentService;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;
import pe.edu.upc.soft.work.platform.worker.forum.application.internal.outboundservices.acl.ExternalIamServiceFromWorkerForum;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Message;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.AddAttachmentsToMessageCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateMessageCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateMessageCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteMessageCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.events.MessagePostedEvent;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.MessageCommandService;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.AttachmentRepository;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.MessageRepository;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.ThreadRepository;

import java.util.Optional;

/**
 * Service implementation for handling Message commands
 */
@Service
public class MessageCommandServiceImpl implements MessageCommandService {
    private final MessageRepository messageRepository;
    private final ExternalIamServiceFromWorkerForum externalIamServiceFromWorkerForum;
    private final ApplicationEventPublisher eventPublisher;
    private final ThreadRepository threadRepository;
    private final AttachmentRepository attachmentRepository;

    /**
     * Constructor for MessageCommandServiceImpl.
     * @param messageRepository the repository for Message persistence
     */
    public MessageCommandServiceImpl(MessageRepository messageRepository,
                                     ExternalIamServiceFromWorkerForum externalIamServiceFromWorkerForum,
                                     ApplicationEventPublisher eventPublisher,
                                     ThreadRepository threadRepository,
                                     AttachmentRepository attachmentRepository) {
        this.messageRepository = messageRepository;
        this.externalIamServiceFromWorkerForum = externalIamServiceFromWorkerForum;
        this.eventPublisher = eventPublisher;
        this.threadRepository = threadRepository;
        this.attachmentRepository = attachmentRepository;

    }

    /**
     * Handles the creation of an Message
     * @param command the command to create an Message
     * @return the generated ID of the new Message
     */
    @Override
    public Long handle(CreateMessageCommand command) {
        if(!externalIamServiceFromWorkerForum.existsUserAccountById(command.userAccountId().userAccountId())){
            throw new NotFoundArgumentException(
                    String.format("[MessageCommandServiceImpl] User Account ID: %s not found in the external IAM service",
                            command.userAccountId().userAccountId())
            );
        }
        if (!threadRepository.existsById(command.threadId())) {
            throw new NotFoundArgumentException(
                    String.format("[MessageCommandServiceImpl] Thread ID: %s not found in the Thread repository",
                            command.threadId())
            );
        }
        var message = new Message(command);
        eventPublisher.publishEvent(new MessagePostedEvent(this, message.getId(), null, message.getUserAccountId()));
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
        if (!threadRepository.existsById(command.threadId())) {
            throw new NotFoundArgumentException(
                    String.format("[MessageCommandServiceImpl] Thread ID: %s not found in the Thread repository",
                            command.threadId())
            );
        }
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

    @Override
    public void handle(AddAttachmentsToMessageCommand command) {
        var attachment = attachmentRepository.findById(command.attachmentId()).orElseThrow(() -> new NotFoundArgumentException(
                String.format("[MessageCommandServiceImpl] Attachment ID: %s not found in the database",
                        command.attachmentId())));
        var message = messageRepository.findById(command.messageId()).orElseThrow(() -> new NotFoundArgumentException(
                String.format("[MessageCommandServiceImpl] Message ID: %s not found in the database",
                        command.messageId())));
        try {
            message.addAttachment(attachment);
            messageRepository.save(message);
        } catch (Exception e) {
            throw new RuntimeException("Error adding Attachment to Message: " + e.getMessage(), e);
        }
    }
}
