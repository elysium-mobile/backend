package pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources;

import pe.edu.upc.soft.work.platform.profile.performance.domain.model.valueobjects.RRHHProfileId;

import java.util.Date;

/**
 * Response object representing a CommentEmployee in the system.
 */
public record CommentEmployeeResponse(
        Long commentemployeeId,
        String title,
        String content,
        Long rrhhProfileId
) {}
