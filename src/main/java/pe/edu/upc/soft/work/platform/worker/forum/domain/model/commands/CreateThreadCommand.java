package pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Message;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.AreaCompanyId;

import java.util.List;
import java.util.Objects;
import java.util.Date;

/**
 * Command to create a new Thread
 */
public record CreateThreadCommand(String title, AreaCompanyId areaCompanyId, Date lastMessage,
                                  List<Message> messages) {

    /**
     * Constructor with validation
     */
    public CreateThreadCommand {
        Objects.requireNonNull(title, "[CreateThreadCommand] title must not be null");
        Objects.requireNonNull(areaCompanyId, "[CreateThreadCommand] areaCompanyId must not be null");
        Objects.requireNonNull(lastMessage, "[CreateThreadCommand] lastMessage must not be null");
    }
}
