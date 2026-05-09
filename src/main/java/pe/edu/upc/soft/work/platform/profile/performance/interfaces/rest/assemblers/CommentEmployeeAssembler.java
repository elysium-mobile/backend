package pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.profile.performance.domain.model.aggregates.CommentEmployee;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.CreateCommentEmployeeCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.UpdateCommentEmployeeCommand;
import pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources.CreateCommentEmployeeRequest;
import pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources.UpdateCommentEmployeeRequest;
import pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources.CommentEmployeeResponse;

public class CommentEmployeeAssembler {

    /**
     * Converts a CreateCommentEmployeeRequest to a CreateCommentEmployeeCommand.
     */
    public static CreateCommentEmployeeCommand toCommandFromRequest(CreateCommentEmployeeRequest request) {
        return new CreateCommentEmployeeCommand(request.title(), request.content(), request.rrhhProfileId());
    }

    /**
     * Converts an UpdateCommentEmployeeRequest to an UpdateCommentEmployeeCommand.
     */
    public static UpdateCommentEmployeeCommand toCommandFromRequest(Long commentemployeeId, UpdateCommentEmployeeRequest request) {
        return new UpdateCommentEmployeeCommand(commentemployeeId, request.title(), request.content(), request.rrhhProfileId());
    }

    /**
     * Converts a CommentEmployee entity to a CommentEmployeeResponse.
     */
    public static CommentEmployeeResponse toResponseFromEntity(CommentEmployee commentemployee) {
        return new CommentEmployeeResponse(commentemployee.getId(), commentemployee.getTitle(), commentemployee.getContent(), commentemployee.getRrhhProfileId());
    }
}
