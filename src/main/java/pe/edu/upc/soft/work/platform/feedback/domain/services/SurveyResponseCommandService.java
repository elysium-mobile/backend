package pe.edu.upc.soft.work.platform.feedback.domain.services;

import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.CreateSurveyResponseCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.DeleteSurveyResponseCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.UpdateSurveyResponseCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.SurveyResponse;

import java.util.Optional;

/**
 * Service interface for handling SurveyResponse-related commands.
 */
public interface SurveyResponseCommandService {

    /**
     * Handles the creation of a new SurveyResponse.
     */
    Long handle(CreateSurveyResponseCommand command);

    /**
     * Handles the update of an existing SurveyResponse.
     */
    Optional<SurveyResponse> handle(UpdateSurveyResponseCommand command);

    /**
     * Handles the deletion of an existing SurveyResponse.
     */
    void handle(DeleteSurveyResponseCommand command);
}
