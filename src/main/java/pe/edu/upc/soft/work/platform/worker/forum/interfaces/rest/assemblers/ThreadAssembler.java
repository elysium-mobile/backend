package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Thread;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateThreadCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateThreadCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.AreaCompanyId;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.CreateThreadRequest;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.ThreadResponse;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.UpdateThreadRequest;

public class ThreadAssembler {

    /**
     * Converts a CreateThreadRequest to a CreateThreadCommand.
     */
    public static CreateThreadCommand toCommandFromRequest(CreateThreadRequest request) {
        return new CreateThreadCommand(request.title(), new AreaCompanyId(request.areaCompanyId()), request.lastMessage());
    }

    /**
     * Converts an UpdateThreadRequest to an UpdateThreadCommand.
     */
    public static UpdateThreadCommand toCommandFromRequest(Long threadId, UpdateThreadRequest request) {
        return new UpdateThreadCommand(threadId, request.title(), new AreaCompanyId(request.areaCompanyId()), request.lastMessage());
    }

    /**
     * Converts a Thread entity to a ThreadResponse.
     */
    public static ThreadResponse toResponseFromEntity(Thread thread) {
        return new ThreadResponse(thread.getId(), thread.getTitle(), thread.getAreaCompanyId().areaCompanyId(), thread.getLastMessage());
    }
}
