package pe.edu.upc.soft.work.platform.feedback.domain.model.commands;

import java.util.Objects;

/**
 * Command to create a new Answer
 */
public record CreateAnswerCommand(Long value, Integer scoreAnswer) {

    /**
     * Constructor with validation
     */
    public CreateAnswerCommand {
        Objects.requireNonNull(value, "[CreateAnswerCommand] value must not be null");
        Objects.requireNonNull(scoreAnswer, "[CreateAnswerCommand] scoreAnswer must not be null");
    }
}
