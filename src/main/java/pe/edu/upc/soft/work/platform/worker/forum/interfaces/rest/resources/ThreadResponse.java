package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Thread;

import java.util.Date;
import java.util.List;

/**
 * Response object representing a Thread in the system.
 */
public record ThreadResponse(
        Long threadId,
        String title,
        Long areaCompanyId,
        Date lastMessage,
        List<MessageResponse> messageResponses
) {}
