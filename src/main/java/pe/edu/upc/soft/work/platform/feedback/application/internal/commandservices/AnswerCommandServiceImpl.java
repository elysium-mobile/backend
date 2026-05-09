package pe.edu.upc.soft.work.platform.feedback.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.Answer;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.CreateAnswerCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.UpdateAnswerCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.DeleteAnswerCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.services.AnswerCommandService;
import pe.edu.upc.soft.work.platform.feedback.infrastructure.persistence.jpa.repositories.AnswerRepository;

import java.util.Optional;

@Service
public class AnswerCommandServiceImpl implements AnswerCommandService {
    private final AnswerRepository answerRepository;

    public AnswerCommandServiceImpl(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

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
