package pe.edu.upc.soft.work.platform.feedback.domain.services;

import pe.edu.upc.soft.work.platform.feedback.domain.model.aggregates.Survey;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.CreateSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.DeleteSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.UpdateSurveyCommand;

import java.util.Optional;

/**
 * Service interface for handling Survey-related commands.
 */
public interface SurveyCommandService {

    /**
     * Handles the creation of a new Survey.
     */
    Long handle(CreateSurveyCommand command);

    /**
     * Handles the update of an existing Survey.
     */
    Optional<Survey> handle(UpdateSurveyCommand command);

    /**
     * Handles the deletion of an existing Survey.
     */
    void handle(DeleteSurveyCommand command);
}
