package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

/**
 * Response object representing a Forum in the system.
 */
public record ForumResponse(
        Long forumId,
        String title,
        String description,
        Long companyId
) {}
