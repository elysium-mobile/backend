package pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.valueobjects.RRHHProfileId;

import java.util.Date;

/**
 * Response object representing a CommentEmployee in the system.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CommentEmployeeResponse(
        Long commentEmployeeId,
        String title,
        String content,
        Long rrhhProfileId,
        Long performanceId
) {}
