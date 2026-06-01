package pe.edu.upc.soft.work.platform.feedback.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.feedback.application.internal.outboundservices.acl.ExternalIamServiceFromFeedback;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.CreateSurveyResponseCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.DeleteSurveyResponseCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.UpdateSurveyResponseCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.SurveyResponse;
import pe.edu.upc.soft.work.platform.feedback.domain.services.SurveyResponseCommandService;
import pe.edu.upc.soft.work.platform.feedback.infrastructure.persistence.jpa.repositories.SurveyRepository;
import pe.edu.upc.soft.work.platform.feedback.infrastructure.persistence.jpa.repositories.SurveyResponseRepository;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;

import java.util.Optional;

/**
 * Service implementation for handling SurveyResponse commands.
 */
@Service
public class SurveyResponseCommandServiceImpl implements SurveyResponseCommandService {
    private final SurveyResponseRepository surveyResponseRepository;
    private final ExternalIamServiceFromFeedback externalIamServiceFromFeedback;
    private final SurveyRepository surveyRepository;

    /**
     * Constructor for SurveyResponseCommandServiceImpl.
     * @param surveyresponseRepository the repository for SurveyResponse persistence
     */
    public SurveyResponseCommandServiceImpl(SurveyResponseRepository surveyresponseRepository,
                                            ExternalIamServiceFromFeedback externalIamServiceFromFeedback,
                                            SurveyRepository surveyRepository) {
        this.surveyResponseRepository = surveyresponseRepository;
        this.externalIamServiceFromFeedback = externalIamServiceFromFeedback;
        this.surveyRepository = surveyRepository;
    }

    /**
     * Handles the creation of an SurveyResponse.
     * @param command the command to create a SurveyResponse
     * @return the generated ID of the new SurveyResponse
     */
    @Override
    public Long handle(CreateSurveyResponseCommand command) {

        if (!surveyRepository.existsById(command.surveyId())){
            throw new NotFoundArgumentException(
                    String.format("[SurveyResponseCommandServiceImpl] Survey ID: %s not found in the external Feedback service",
                            command.surveyId()));
        }
        if(!externalIamServiceFromFeedback.existEmployeeProfileById(command.employeeProfileId().employeeProfileId())){
            throw new NotFoundArgumentException(
                    String.format("[SurveyResponseCommandServiceImpl] Employee Profile ID: %s not found in the external IAM service",
                            command.employeeProfileId().employeeProfileId()));
        }
        var surveyResponse = new SurveyResponse(command);
        try {
            surveyResponseRepository.save(surveyResponse);
        } catch (Exception e) {
            throw new RuntimeException("Error creating SurveyResponse: " + e.getMessage(), e);
        }
        return surveyResponse.getId();
    }

    /**
     * Handles the update of an existing SurveyResponse.
     * @param command the command to update an SurveyResponse
     * @return the updated SurveyResponse as a Optional
     */
    @Override
    public Optional<SurveyResponse> handle(UpdateSurveyResponseCommand command) {
        var surveyresponseId = command.surveyresponseId();
        if (!this.surveyResponseRepository.existsById(surveyresponseId)) {
            throw new RuntimeException("SurveyResponse with ID " + surveyresponseId + " does not exist.");
        }

        var surveyresponseToUpdate = this.surveyResponseRepository.findById(surveyresponseId).get();
        surveyresponseToUpdate.updateSurveyResponse(command);
        try {
            var updatedSurveyResponse = this.surveyResponseRepository.save(surveyresponseToUpdate);
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
        if (!surveyResponseRepository.existsById(command.surveyresponseId())) {
            throw new RuntimeException("SurveyResponse with ID " + command.surveyresponseId() + " does not exist.");
        }
        try {
            surveyResponseRepository.deleteById(command.surveyresponseId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting SurveyResponse: " + e.getMessage(), e);
        }
    }
}
