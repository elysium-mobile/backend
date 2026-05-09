package pe.edu.upc.soft.work.platform.feedback.domain.model.commands;

/**
 * Command to delete a Answer
 */
public record DeleteAnswerCommand(Long answerId) {

    /**
     * Constructor with validation
     */
    public DeleteAnswerCommand {
        if (answerId == null || answerId <= 0) {
            throw new IllegalArgumentException("[DeleteAnswerCommand] answerId must be a positive number");
        }
    }
}
