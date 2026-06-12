package pe.edu.upc.soft.work.platform.feedback.domain.services;

import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.CreateAnswerCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.DeleteAnswerCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.UpdateAnswerCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.Answer;

import java.util.Optional;

/**
 * Service interface for handling Answer-related commands.
 */
public interface AnswerCommandService {

    /**
     * Handles the creation of a new Answer.
     */
    Long handle(CreateAnswerCommand command);

    /**
     * Handles the update of an existing Answer.
     */
    Optional<Answer> handle(UpdateAnswerCommand command);

    /**
     * Handles the deletion of an existing Answer.
     */
    void handle(DeleteAnswerCommand command);
}
