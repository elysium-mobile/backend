package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Forum;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateForumCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateForumCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.CompanyId;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.CreateForumRequest;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.ForumResponse;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.UpdateForumRequest;

public class ForumAssembler {

    /**
     * Converts a CreateForumRequest to a CreateForumCommand.
     */
    public static CreateForumCommand toCommandFromRequest(CreateForumRequest request) {
        return new CreateForumCommand(request.title(), request.description(), new CompanyId(request.companyId()));
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
        return new ForumResponse(forum.getId(), forum.getTitle(), forum.getDescription(), forum.getCompanyId().companyId());
    }
}
