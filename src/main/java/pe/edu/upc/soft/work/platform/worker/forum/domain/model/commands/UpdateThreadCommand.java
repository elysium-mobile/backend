package pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.AreaCompanyId;

import java.util.Objects;
import java.util.Date;

/**
 * Command to update an existing Thread
 */
public record UpdateThreadCommand(Long threadId, String title, AreaCompanyId areaCompanyId, Date lastMessage, Long categoryId, Integer messageCount) {

    /**
     * Constructor with validation
     */
    public UpdateThreadCommand {
        Objects.requireNonNull(threadId, "[UpdateThreadCommand] threadId must not be null");
    }
}
