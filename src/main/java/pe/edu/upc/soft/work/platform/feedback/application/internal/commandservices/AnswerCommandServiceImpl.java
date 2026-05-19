package pe.edu.upc.soft.work.platform.feedback.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.Answer;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.CreateAnswerCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.UpdateAnswerCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.DeleteAnswerCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.services.AnswerCommandService;
import pe.edu.upc.soft.work.platform.feedback.infrastructure.persistence.jpa.repositories.AnswerRepository;

import java.util.Optional;

/**
 * Service implementation for handling Answer commands.
 */
@Service
public class AnswerCommandServiceImpl implements AnswerCommandService {
    private final AnswerRepository answerRepository;

    /**
     * Constructor for AnswerCommandServiceImpl.
     * @param answerRepository the repository for Answer persistence
     */
    public AnswerCommandServiceImpl(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    /**
     * Handles the creation of an Answer.
     * @param command the command to create an Answer
     * @return the generated ID of the new Answer
     */
    @Override
    public Long handle(CreateAnswerCommand command) {
        var answer = new Answer(command);
        try {
            answerRepository.save(answer);
        } catch (Exception e) {
            throw new RuntimeException("Error creating Answer: " + e.getMessage(), e);
        }
        return answer.getId();
    }

    /**
     * Handles the update of an existing Answer
     * @param command the command to update an Answer
     * @return the updated Answer as an Optional
     */
    @Override
    public Optional<Answer> handle(UpdateAnswerCommand command) {
        var answerId = command.answerId();
        if (!this.answerRepository.existsById(answerId)) {
            throw new RuntimeException("Answer with ID " + answerId + " does not exist.");
        }

        var answerToUpdate = this.answerRepository.findById(answerId).get();
        answerToUpdate.updateAnswer(command);
        try {
            var updatedAnswer = this.answerRepository.save(answerToUpdate);
            return Optional.of(updatedAnswer);
        } catch (Exception e) {
            throw new RuntimeException("Error updating Answer: " + e.getMessage(), e);
        }
    }

    /**
     * Handles the deletion of a Answer
     * @param command the command to delete an Answer
     */
    @Override
    public void handle(DeleteAnswerCommand command) {
        if (!answerRepository.existsById(command.answerId())) {
            throw new RuntimeException("Answer with ID " + command.answerId() + " does not exist.");
        }
        try {
            answerRepository.deleteById(command.answerId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting Answer: " + e.getMessage(), e);
        }
    }
}
