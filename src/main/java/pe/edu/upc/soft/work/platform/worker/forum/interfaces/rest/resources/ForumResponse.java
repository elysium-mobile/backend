package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import java.util.List;

/**
 * Response object representing a Forum in the system.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ForumResponse(
        Long forumId,
        String title,
        String description,
        Long companyId,
        List<CategoryResponse> categories
) {}
