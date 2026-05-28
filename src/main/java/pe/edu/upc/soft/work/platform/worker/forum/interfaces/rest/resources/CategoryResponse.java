package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

/**
 * Response object representing a Category in the system.
 */
public record CategoryResponse(
        Long categoryId,
        String title,
        String description
) {}
