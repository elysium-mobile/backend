package pe.edu.upc.soft.work.platform.feedback.domain.services;

import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.QuestionSurvey;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.CreateQuestionSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.UpdateQuestionSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.DeleteQuestionSurveyCommand;

import java.util.Optional;

/**
 * Service interface for handling QuestionSurvey-related commands.
 */
public interface QuestionSurveyCommandService {

    /**
     * Handles the creation of a new QuestionSurvey.
     */
    Long handle(CreateQuestionSurveyCommand command);

    /**
     * Handles the update of an existing QuestionSurvey.
     */
    Optional<QuestionSurvey> handle(UpdateQuestionSurveyCommand command);

    /**
     * Handles the deletion of an existing QuestionSurvey.
     */
    void handle(DeleteQuestionSurveyCommand command);
}
