package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

/**
 * Response object representing a Message in the system.
 */
public record MessageResponse(
        Long messageId,
        Long userAccountId,
        String contentMessage
) {}
