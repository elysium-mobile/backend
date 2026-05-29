package pe.edu.upc.soft.work.platform.feedback.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.CreateQuestionSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.DeleteQuestionSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.UpdateQuestionSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.QuestionSurvey;
import pe.edu.upc.soft.work.platform.feedback.domain.services.QuestionSurveyCommandService;
import pe.edu.upc.soft.work.platform.feedback.infrastructure.persistence.jpa.repositories.QuestionSurveyRepository;

import java.util.Optional;

/**
 * Service implementation for handling QuestionSurvey commands
 */
@Service
public class QuestionSurveyCommandServiceImpl implements QuestionSurveyCommandService {
    private final QuestionSurveyRepository questionsurveyRepository;

    /**
     * Constructor for QuestionSurveyCommandServiceImpl
     * @param questionsurveyRepository the repository for QuestionSurvey persistence
     */
    public QuestionSurveyCommandServiceImpl(QuestionSurveyRepository questionsurveyRepository) {
        this.questionsurveyRepository = questionsurveyRepository;
    }


    /**
     * Handles the creation of an QuestionSurvey
     * @param command the command to create an QuestionSurvey
     * @return  the generated IF of the new QuestionSurvey
     */
    @Override
    public Long handle(CreateQuestionSurveyCommand command) {
        var questionsurvey = new QuestionSurvey(command);
        try {
            questionsurveyRepository.save(questionsurvey);
        } catch (Exception e) {
            throw new RuntimeException("Error creating QuestionSurvey: " + e.getMessage(), e);
        }
        return questionsurvey.getId();
    }

    /**
     * Handles the update of an existing QuestionSurvey
     * @param command the command to update an QuestionSurvey
     * @return the updated QuestionSurvey as an Optional
     */
    @Override
    public Optional<QuestionSurvey> handle(UpdateQuestionSurveyCommand command) {
        var questionsurveyId = command.questionSurveyId();
        if (!this.questionsurveyRepository.existsById(questionsurveyId)) {
            throw new RuntimeException("QuestionSurvey with ID " + questionsurveyId + " does not exist.");
        }

        var questionsurveyToUpdate = this.questionsurveyRepository.findById(questionsurveyId).get();
        questionsurveyToUpdate.updateQuestionSurvey(command);
        try {
            var updatedQuestionSurvey = this.questionsurveyRepository.save(questionsurveyToUpdate);
            return Optional.of(updatedQuestionSurvey);
        } catch (Exception e) {
            throw new RuntimeException("Error updating QuestionSurvey: " + e.getMessage(), e);
        }
    }

    /**
     * Handles the deletion of an QuestionSurvey
     * @param command the command to delete an QuestionSurvey
     */
    @Override
    public void handle(DeleteQuestionSurveyCommand command) {
        if (!questionsurveyRepository.existsById(command.questionsurveyId())) {
            throw new RuntimeException("QuestionSurvey with ID " + command.questionsurveyId() + " does not exist.");
        }
        try {
            questionsurveyRepository.deleteById(command.questionsurveyId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting QuestionSurvey: " + e.getMessage(), e);
        }
    }
}
