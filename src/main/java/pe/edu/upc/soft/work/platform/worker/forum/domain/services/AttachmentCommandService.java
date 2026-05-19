package pe.edu.upc.soft.work.platform.worker.forum.domain.services;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Attachment;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateAttachmentCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateAttachmentCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteAttachmentCommand;

import java.util.Optional;

/**
 * Service interface for handling Attachment-related commands.
 */
public interface AttachmentCommandService {

    /**
     * Handles the creation of a new Attachment.
     */
    Long handle(CreateAttachmentCommand command);

    /**
     * Handles the update of an existing Attachment.
     */
    Optional<Attachment> handle(UpdateAttachmentCommand command);

    /**
     * Handles the deletion of an existing Attachment.
     */
    void handle(DeleteAttachmentCommand command);
}
