package pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands;

public record AddAttachmentsToMessageCommand(Long attachmentId, Long messageId) {
}
