package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

import java.util.Date;

/**
 * Response object representing a Thread in the system.
 */
public record ThreadResponse(
        Long threadId,
        String title,
        Long areaCompanyId,
        Date lastMessage
) {}
