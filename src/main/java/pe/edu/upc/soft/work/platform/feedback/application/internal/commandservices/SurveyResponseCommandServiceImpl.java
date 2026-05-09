package pe.edu.upc.soft.work.platform.feedback.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.SurveyResponse;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.CreateSurveyResponseCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.UpdateSurveyResponseCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.DeleteSurveyResponseCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.services.SurveyResponseCommandService;
import pe.edu.upc.soft.work.platform.feedback.infrastructure.persistence.jpa.repositories.SurveyResponseRepository;

import java.util.Optional;

@Service
public class SurveyResponseCommandServiceImpl implements SurveyResponseCommandService {
    private final SurveyResponseRepository surveyresponseRepository;

    public SurveyResponseCommandServiceImpl(SurveyResponseRepository surveyresponseRepository) {
        this.surveyresponseRepository = surveyresponseRepository;
    }

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
