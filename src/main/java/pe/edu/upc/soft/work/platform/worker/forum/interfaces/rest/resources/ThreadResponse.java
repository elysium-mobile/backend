package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Thread;

import java.util.Date;
import java.util.List;

/**
 * Response object representing a Thread in the system.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ThreadResponse(
        Long threadId,
        String title,
        Long areaCompanyId,
        Date lastMessage,
        Long categoryId,
        Integer messageCount,
        List<MessageResponse> messageResponses
) {}
