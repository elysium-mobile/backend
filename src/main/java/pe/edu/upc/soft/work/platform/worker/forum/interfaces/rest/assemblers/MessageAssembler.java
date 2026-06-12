package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Message;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.AddAttachmentsToMessageCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateMessageCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateMessageCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.UserAccountId;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.*;

import java.util.ArrayList;
import java.util.List;

public class MessageAssembler {

    /**
     * Converts a CreateMessageRequest to a CreateMessageCommand.
     */
    public static CreateMessageCommand toCommandFromRequest(CreateMessageRequest request) {
        return new CreateMessageCommand(new UserAccountId(request.userAccountId()), request.contentMessage(), request.threadId(),new ArrayList<>());
    }

    /**
     * Converts an UpdateMessageRequest to an UpdateMessageCommand.
     */
    public static UpdateMessageCommand toCommandFromRequest(Long messageId, UpdateMessageRequest request) {
        return new UpdateMessageCommand(messageId, new UserAccountId(request.userAccountId()), request.contentMessage(), request.threadId());
    }

    public static AddAttachmentsToMessageCommand toCommandFromRequest(Long messageId, AddAssetToMessageRequest request){
        return new AddAttachmentsToMessageCommand(request.attachmentId(), messageId);
    }

    /**
     * Converts a Message entity to a MessageResponse.
     */
    public static MessageResponse toResponseFromEntity(Message message) {
        List<AssetResponse> assetRespons = message.getAssets().stream()
                .map(attachment -> new AssetResponse(
                        attachment.getId(),
                        attachment.getMessageId(),
                        attachment.getName(),
                        attachment.getUrl(),
                        attachment.getFileSize(),
                        attachment.getFileType(),
                    attachment.isViewable(), attachment.isReadable()
                ))
                .toList();

        return new MessageResponse(message.getId(), message.getUserAccountId().userAccountId(), message.getContentMessage(),message.getThreadId(), assetRespons);
    }
}
