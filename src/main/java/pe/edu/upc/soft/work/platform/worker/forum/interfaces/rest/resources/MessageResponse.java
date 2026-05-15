package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.UserAccountId;

import java.util.Date;

/**
 * Response object representing a Message in the system.
 */
public record MessageResponse(
        Long messageId,
        Long userAccountId,
        String contentMessage
) {}
