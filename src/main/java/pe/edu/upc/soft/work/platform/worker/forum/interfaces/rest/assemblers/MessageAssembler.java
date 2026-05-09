package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Message;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateMessageCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateMessageCommand;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.CreateMessageRequest;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.UpdateMessageRequest;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.MessageResponse;

public class MessageAssembler {

    /**
     * Converts a CreateMessageRequest to a CreateMessageCommand.
     */
    public static CreateMessageCommand toCommandFromRequest(CreateMessageRequest request) {
        return new CreateMessageCommand(request.userAccountId(), request.contentMessage());
    }

    /**
     * Converts an UpdateMessageRequest to an UpdateMessageCommand.
     */
    public static UpdateMessageCommand toCommandFromRequest(Long messageId, UpdateMessageRequest request) {
        return new UpdateMessageCommand(messageId, request.userAccountId(), request.contentMessage());
    }

    /**
     * Converts a Message entity to a MessageResponse.
     */
    public static MessageResponse toResponseFromEntity(Message message) {
        return new MessageResponse(message.getId(), message.getUserAccountId(), message.getContentMessage());
    }
}
