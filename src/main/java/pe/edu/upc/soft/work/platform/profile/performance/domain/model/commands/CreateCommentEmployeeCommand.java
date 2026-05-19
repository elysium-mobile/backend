package pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands;

import pe.edu.upc.soft.work.platform.profile.performance.domain.model.valueobjects.RRHHProfileId;

import java.util.Objects;
import java.util.Date;

/**
 * Command to create a new CommentEmployee
 */
public record CreateCommentEmployeeCommand(String title, String content, RRHHProfileId rrhhProfileId) {

    /**
     * Constructor with validation
     */
    public CreateCommentEmployeeCommand {
        Objects.requireNonNull(title, "[CreateCommentEmployeeCommand] title must not be null");
        Objects.requireNonNull(content, "[CreateCommentEmployeeCommand] content must not be null");
        Objects.requireNonNull(rrhhProfileId, "[CreateCommentEmployeeCommand] rrhhProfileId must not be null");
    }
}
