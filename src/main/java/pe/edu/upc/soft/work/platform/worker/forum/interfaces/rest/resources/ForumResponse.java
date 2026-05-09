package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.CompanyId;

import java.util.Date;

/**
 * Response object representing a Forum in the system.
 */
public record ForumResponse(
        Long forumId,
        String title,
        String description,
        CompanyId companyId
) {}
