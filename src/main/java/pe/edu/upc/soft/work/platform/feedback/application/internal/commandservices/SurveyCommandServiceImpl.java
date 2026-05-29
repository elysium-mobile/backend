package pe.edu.upc.soft.work.platform.feedback.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.feedback.domain.model.aggregates.Survey;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.CreateSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.DeleteSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.UpdateSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.services.SurveyCommandService;
import pe.edu.upc.soft.work.platform.feedback.infrastructure.persistence.jpa.repositories.SurveyRepository;

import java.util.Optional;

/**
 * Services implementation for handling Survey commands
 */
@Service
public class SurveyCommandServiceImpl implements SurveyCommandService {
    private final SurveyRepository surveyRepository;

    /**
     * Constructor for SurveyCommandServiceImpl
     * @param surveyRepository the repository for Survey persistence
     */
    public SurveyCommandServiceImpl(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    /**
     * Handles the creation of an Survey
     * @param command the command to create a Survey
     * @return the generated ID of the new Survey
     */
    @Override
    public Long handle(CreateSurveyCommand command) {
        var survey = new Survey(command);
        try {
            surveyRepository.save(survey);
        } catch (Exception e) {
            throw new RuntimeException("Error creating Survey: " + e.getMessage(), e);
        }
        return survey.getId();
    }

    /**
     * Handles the update of an existing Survey
     * @param command the command to update an Survey
     * @return the updated Survey as an Optional
     */
    @Override
    public Optional<Survey> handle(UpdateSurveyCommand command) {
        var surveyId = command.surveyId();
        if (!this.surveyRepository.existsById(surveyId)) {
            throw new RuntimeException("Survey with ID " + surveyId + " does not exist.");
        }

        var surveyToUpdate = this.surveyRepository.findById(surveyId).get();
        surveyToUpdate.updateSurvey(command);
        try {
            var updatedSurvey = this.surveyRepository.save(surveyToUpdate);
            return Optional.of(updatedSurvey);
        } catch (Exception e) {
            throw new RuntimeException("Error updating Survey: " + e.getMessage(), e);
        }
    }

    /**
     * Handles the deletion of a Survey
     * @param command the command to delete a Survey
     */
    @Override
    public void handle(DeleteSurveyCommand command) {
        if (!surveyRepository.existsById(command.surveyId())) {
            throw new RuntimeException("Survey with ID " + command.surveyId() + " does not exist.");
        }
        try {
            surveyRepository.deleteById(command.surveyId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting Survey: " + e.getMessage(), e);
        }
    }
}
