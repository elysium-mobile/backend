package pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands;

public record AddAssetToMessageCommand(Long attachmentId, Long messageId) {
}
