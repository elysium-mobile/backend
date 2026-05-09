package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Forum;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateForumCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateForumCommand;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.CreateForumRequest;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.UpdateForumRequest;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.ForumResponse;

public class ForumAssembler {

    /**
     * Converts a CreateForumRequest to a CreateForumCommand.
     */
    public static CreateForumCommand toCommandFromRequest(CreateForumRequest request) {
        return new CreateForumCommand(request.title(), request.description(), request.companyId());
    }

    /**
     * Converts an UpdateForumRequest to an UpdateForumCommand.
     */
    public static UpdateForumCommand toCommandFromRequest(Long forumId, UpdateForumRequest request) {
        return new UpdateForumCommand(forumId, request.title(), request.description(), request.companyId());
    }

    /**
     * Converts a Forum entity to a ForumResponse.
     */
    public static ForumResponse toResponseFromEntity(Forum forum) {
        return new ForumResponse(forum.getId(), forum.getTitle(), forum.getDescription(), forum.getCompanyId());
    }
}
