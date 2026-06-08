package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.AddThreadToCategoryCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateCategoryCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateCategoryCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Category;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.*;

import java.util.ArrayList;
import java.util.List;

public class CategoryAssembler {

    /**
     * Converts a CreateCategoryRequest to a CreateCategoryCommand.
     */
    public static CreateCategoryCommand toCommandFromRequest(CreateCategoryRequest request) {
        return new CreateCategoryCommand(request.title(), request.description(), request.forumId(), new ArrayList<>());
    }

    /**
     * Converts an UpdateCategoryRequest to an UpdateCategoryCommand.
     */
    public static UpdateCategoryCommand toCommandFromRequest(Long categoryId, UpdateCategoryRequest request) {
        return new UpdateCategoryCommand(categoryId, request.title(), request.description(),request.forumId());
    }

    public static AddThreadToCategoryCommand toCommandFromRequest(Long categoryId, AddThreadToCategoryRequest request) {
        return new AddThreadToCategoryCommand(request.threadId(), categoryId);
    }

    /**
     * Converts a Category entity to a CategoryResponse.
     */
    public static CategoryResponse toResponseFromEntity(Category category) {
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
                                        message.getThreadId(),
                                        attachmentResponses
                                );
                            })
                            .toList();
                    return new ThreadResponse(
                            thread.getId(),
                            thread.getTitle(),
                            thread.getAreaCompanyId().areaCompanyId(),
                            thread.getLastMessage(),
                            thread.getCategoryId(),
                            thread.getMessageCount(),
                            messageResponses
                    );
                })
                .toList();

        return new CategoryResponse(category.getId(), category.getTitle(), category.getDescription(), category.getForumId(), threadResponses);
    }
}
