package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateAttachmentCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateAttachmentCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Attachment;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.AttachmentResponse;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.CreateAttachmentRequest;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.UpdateAttachmentRequest;

public class AttachmentAssembler {

    /**
     * Converts a CreateAttachmentRequest to a CreateAttachmentCommand.
     */
    public static CreateAttachmentCommand toCommandFromRequest(CreateAttachmentRequest request) {
        return new CreateAttachmentCommand(request.messageId(), request.name(), request.url(), request.fileSize(), request.fileType());
    }

    /**
     * Converts an UpdateAttachmentRequest to an UpdateAttachmentCommand.
     */
    public static UpdateAttachmentCommand toCommandFromRequest(Long attachmentId, UpdateAttachmentRequest request) {
        return new UpdateAttachmentCommand(attachmentId, request.messageId(), request.name(), request.url(), request.fileSize(), request.fileType());
    }

    /**
     * Converts an Attachment entity to an AttachmentResponse.
     */
    public static AttachmentResponse toResponseFromEntity(Attachment attachment) {
        return new AttachmentResponse(attachment.getId(), attachment.getMessageId(), attachment.getName(), attachment.getUrl(), attachment.getFileSize(), attachment.getFileType());
    }
}
