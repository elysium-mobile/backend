package pe.edu.upc.soft.work.platform.feedback.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.SurveyResponse;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.CreateSurveyResponseCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.UpdateSurveyResponseCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.DeleteSurveyResponseCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.services.SurveyResponseCommandService;
import pe.edu.upc.soft.work.platform.feedback.infrastructure.persistence.jpa.repositories.SurveyResponseRepository;

import java.util.Optional;

/**
 * Service implementation for handling SurveyResponse commands.
 */
@Service
public class SurveyResponseCommandServiceImpl implements SurveyResponseCommandService {
    private final SurveyResponseRepository surveyresponseRepository;

    /**
     * Constructor for SurveyResponseCommandServiceImpl.
     * @param surveyresponseRepository the repository for SurveyResponse persistence
     */
    public SurveyResponseCommandServiceImpl(SurveyResponseRepository surveyresponseRepository) {
        this.surveyresponseRepository = surveyresponseRepository;
    }

    /**
     * Handles the creation of an SurveyResponse.
     * @param command the command to create a SurveyResponse
     * @return the generated ID of the new SurveyResponse
     */
    @Override
    public Long handle(CreateSurveyResponseCommand command) {
        var surveyresponse = new SurveyResponse(command);
        try {
            surveyresponseRepository.save(surveyresponse);
        } catch (Exception e) {
            throw new RuntimeException("Error creating SurveyResponse: " + e.getMessage(), e);
        }
        return surveyresponse.getId();
    }

    /**
     * Handles the update of an existing SurveyResponse.
     * @param command the command to update an SurveyResponse
     * @return the updated SurveyResponse as a Optional
     */
    @Override
    public Optional<SurveyResponse> handle(UpdateSurveyResponseCommand command) {
        var surveyresponseId = command.surveyresponseId();
        if (!this.surveyresponseRepository.existsById(surveyresponseId)) {
            throw new RuntimeException("SurveyResponse with ID " + surveyresponseId + " does not exist.");
        }

        var surveyresponseToUpdate = this.surveyresponseRepository.findById(surveyresponseId).get();
        surveyresponseToUpdate.updateSurveyResponse(command);
        try {
            var updatedSurveyResponse = this.surveyresponseRepository.save(surveyresponseToUpdate);
            return Optional.of(updatedSurveyResponse);
        } catch (Exception e) {
            throw new RuntimeException("Error updating SurveyResponse: " + e.getMessage(), e);
        }
    }

    /**
     * Handles the deletion of an SurveyResponse
     * @param command the command to delete an SurveyResponse
     */
    @Override
    public void handle(DeleteSurveyResponseCommand command) {
        if (!surveyresponseRepository.existsById(command.surveyresponseId())) {
            throw new RuntimeException("SurveyResponse with ID " + command.surveyresponseId() + " does not exist.");
        }
        try {
            surveyresponseRepository.deleteById(command.surveyresponseId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting SurveyResponse: " + e.getMessage(), e);
        }
    }
}
