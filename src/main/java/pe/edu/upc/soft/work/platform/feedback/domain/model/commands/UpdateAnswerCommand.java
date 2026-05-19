package pe.edu.upc.soft.work.platform.feedback.domain.model.commands;

import java.util.Objects;
import java.util.Date;

/**
 * Command to update an existing Answer
 */
public record UpdateAnswerCommand(Long answerId, Long value, Integer scoreAnswer) {

    /**
     * Constructor with validation
     */
    public UpdateAnswerCommand {
        Objects.requireNonNull(answerId, "[UpdateAnswerCommand] answerId must not be null");
    }
}
