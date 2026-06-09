package pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Asset;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.UserAccountId;

import java.util.List;
import java.util.Objects;

/**
 * Command to create a new Message
 */
public record CreateMessageCommand(UserAccountId userAccountId, String contentMessage,Long threadId,
                                   List<Asset> assets) {

    /**
     * Constructor with validation
     */
    public CreateMessageCommand {
        Objects.requireNonNull(userAccountId, "[CreateMessageCommand] userAccountId must not be null");
        Objects.requireNonNull(contentMessage, "[CreateMessageCommand] contentMessage must not be null");
    }
}
