package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Forum;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateForumCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateForumCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.CompanyId;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.*;

import java.util.ArrayList;
import java.util.List;

public class ForumAssembler {

    /**
     * Converts a CreateForumRequest to a CreateForumCommand.
     */
    public static CreateForumCommand toCommandFromRequest(CreateForumRequest request) {
        return new CreateForumCommand(request.title(), request.description(), new CompanyId(request.companyId()), new ArrayList<>());
    }

    /**
     * Converts an UpdateForumRequest to an UpdateForumCommand.
     */
    public static UpdateForumCommand toCommandFromRequest(Long forumId, UpdateForumRequest request) {
        return new UpdateForumCommand(forumId, request.title(), request.description(), new CompanyId(request.companyId()));
    }

    /**
     * Converts a Forum entity to a ForumResponse.
     */
    public static ForumResponse toResponseFromEntity(Forum forum) {
        List<CategoryResponse> categoryResponses = forum.getCategories().stream()
                .map(category -> {
                    List<ThreadResponse> threadResponses = category.getThreads().stream()
                            .map(thread -> {
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
                                                    attachmentResponses
                                            );
                                        })
                                        .toList();
                                return new ThreadResponse(
                                        thread.getId(),
                                        thread.getTitle(),
                                        thread.getAreaCompanyId().areaCompanyId(),
                                        thread.getLastMessage(),
                                        messageResponses
                                );
                            })
                            .toList();
                    return new CategoryResponse(
                            category.getId(),
                            category.getTitle(),
                            category.getDescription(),
                            threadResponses
                    );
                })
                .toList();

        return new ForumResponse(forum.getId(), forum.getTitle(), forum.getDescription(), forum.getCompanyId().companyId(), categoryResponses);
    }
}
