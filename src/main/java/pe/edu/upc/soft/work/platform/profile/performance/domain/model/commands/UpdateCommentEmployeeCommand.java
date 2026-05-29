package pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands;

import pe.edu.upc.soft.work.platform.profile.performance.domain.model.valueobjects.RRHHProfileId;

import java.util.Objects;
import java.util.Date;

/**
 * Command to update an existing CommentEmployee
 */
public record UpdateCommentEmployeeCommand(Long commentEmployeeId, String title, String content, RRHHProfileId rrhhProfileId) {

    /**
     * Constructor with validation
     */
    public UpdateCommentEmployeeCommand {
        Objects.requireNonNull(commentEmployeeId, "[UpdateCommentEmployeeCommand] commentemployeeId must not be null");
    }
}
