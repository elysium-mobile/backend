package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

import java.util.List;

/**
 * Response object representing a Category in the system.
 */
public record CategoryResponse(
        Long categoryId,
        String title,
        String description,
        Long forumId,
        List<ThreadResponse> threads
) {}
