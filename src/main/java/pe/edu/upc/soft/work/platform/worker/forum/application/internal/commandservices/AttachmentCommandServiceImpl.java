package pe.edu.upc.soft.work.platform.worker.forum.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Attachment;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateAttachmentCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateAttachmentCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteAttachmentCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.AttachmentCommandService;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.AttachmentRepository;

import java.util.Optional;

/**
 * Service implementation for handling Attachment commands
 */
@Service
public class AttachmentCommandServiceImpl implements AttachmentCommandService {
    private final AttachmentRepository attachmentRepository;

    /**
     * Constructor for AttachmentCommandServiceImpl.
     * @param attachmentRepository the repository for Attachment persistence
     */
    public AttachmentCommandServiceImpl(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    /**
     * Handles the creation of an Attachment
     * @param command the command to create an Attachment
     * @return the generated ID of the new Attachment
     */
    @Override
    public Long handle(CreateAttachmentCommand command) {
        var attachment = new Attachment(command);
        try {
            attachmentRepository.save(attachment);
        } catch (Exception e) {
            throw new RuntimeException("Error creating Attachment: " + e.getMessage(), e);
        }
        return attachment.getId();
    }

    /**
     * Handles the update of an existing Attachment
     * @param command the command to update an Attachment
     * @return the updated Attachment as an Optional
     */
    @Override
    public Optional<Attachment> handle(UpdateAttachmentCommand command) {
        var attachmentId = command.attachmentId();
        if (!this.attachmentRepository.existsById(attachmentId)) {
            throw new RuntimeException("Attachment with ID " + attachmentId + " does not exist.");
        }

        var attachmentToUpdate = this.attachmentRepository.findById(attachmentId).get();
        attachmentToUpdate.updateAttachment(command);
        try {
            var updatedAttachment = this.attachmentRepository.save(attachmentToUpdate);
            return Optional.of(updatedAttachment);
        } catch (Exception e) {
            throw new RuntimeException("Error updating Attachment: " + e.getMessage(), e);
        }
    }

    /**
     * Handles the deletion of an Attachment
     * @param command the command to delete an Attachment
     */
    @Override
    public void handle(DeleteAttachmentCommand command) {
        if (!attachmentRepository.existsById(command.attachmentId())) {
            throw new RuntimeException("Attachment with ID " + command.attachmentId() + " does not exist.");
        }
        try {
            attachmentRepository.deleteById(command.attachmentId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting Attachment: " + e.getMessage(), e);
        }
    }
}
