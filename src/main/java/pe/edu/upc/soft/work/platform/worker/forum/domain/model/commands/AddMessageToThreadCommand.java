package pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands;

public record AddMessageToThreadCommand(Long messageId, Long threadId) {
}
