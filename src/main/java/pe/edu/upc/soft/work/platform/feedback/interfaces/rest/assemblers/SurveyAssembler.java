package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.feedback.domain.model.aggregates.Survey;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.CreateSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.UpdateSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.valueObjects.TargetType;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.CreateSurveyRequest;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.UpdateSurveyRequest;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.SurveyResponse;

public class SurveyAssembler {

    /**
     * Converts a CreateSurveyRequest to a CreateSurveyCommand.
     */
    public static CreateSurveyCommand toCommandFromRequest(CreateSurveyRequest request) {
        return new CreateSurveyCommand(request.title(), request.description(), TargetType.valueOf(request.targetType()), request.expirationTime());
    }

    /**
     * Converts an UpdateSurveyRequest to an UpdateSurveyCommand.
     */
    public static UpdateSurveyCommand toCommandFromRequest(Long surveyId, UpdateSurveyRequest request) {
        return new UpdateSurveyCommand(surveyId, request.title(), request.description(), TargetType.valueOf(request.targetType()), request.expirationTime());
    }

    /**
     * Converts a Survey entity to a SurveyResponse.
     */
    public static SurveyResponse toResponseFromEntity(Survey survey) {
        return new SurveyResponse(survey.getId(), survey.getTitle(), survey.getDescription(), survey.getTargetType().name(), survey.getExpirationTime());
    }
}
