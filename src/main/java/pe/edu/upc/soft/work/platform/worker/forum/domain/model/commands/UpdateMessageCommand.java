package pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.UserAccountId;

import java.util.Objects;
import java.util.Date;

/**
 * Command to update an existing Message
 */
public record UpdateMessageCommand(Long messageId, UserAccountId userAccountId, String contentMessage, Long threadId) {

    /**
     * Constructor with validation
     */
    public UpdateMessageCommand {
        Objects.requireNonNull(messageId, "[UpdateMessageCommand] messageId must not be null");
    }
}
