package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Thread;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateThreadCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateThreadCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.AreaCompanyId;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.*;

import java.util.ArrayList;
import java.util.List;

public class ThreadAssembler {

    /**
     * Converts a CreateThreadRequest to a CreateThreadCommand.
     */
    public static CreateThreadCommand toCommandFromRequest(CreateThreadRequest request) {
        return new CreateThreadCommand(request.title(), new AreaCompanyId(request.areaCompanyId()), request.lastMessage(),request.categoryId(), new ArrayList<>());
    }

    /**
     * Converts an UpdateThreadRequest to an UpdateThreadCommand.
     */
    public static UpdateThreadCommand toCommandFromRequest(Long threadId, UpdateThreadRequest request) {
        return new UpdateThreadCommand(threadId, request.title(), new AreaCompanyId(request.areaCompanyId()), request.lastMessage(), request.categoryId());
    }

    /**
     * Converts a Thread entity to a ThreadResponse.
     */
    public static ThreadResponse toResponseFromEntity(Thread thread) {
        List<MessageResponse> messageResponses = thread.getMessages().stream()
                .map(message -> {
                    List<AttachmentResponse> attachmentResponses = message.getAttachments().stream()
                            .map(attachment -> new AttachmentResponse(
                                    attachment.getId(),
                                    attachment.getMessageId(),
                                    attachment.getName(),
                                    attachment.getUrl(),
                                    attachment.getFileSize(),
                                    attachment.getFileType()
                            ))
                            .toList();
                    return new MessageResponse(
                            message.getId(),
                            message.getUserAccountId().userAccountId(),
                            message.getContentMessage(),
                            message.getThreadId(),
                            attachmentResponses
                    );
                })
                .toList();

        return new ThreadResponse(thread.getId(), thread.getTitle(), thread.getAreaCompanyId().areaCompanyId(), thread.getLastMessage(), thread.getCategoryId(), messageResponses);
    }
}
