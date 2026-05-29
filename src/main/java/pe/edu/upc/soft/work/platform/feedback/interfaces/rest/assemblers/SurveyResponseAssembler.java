package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.CreateSurveyResponseCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.UpdateSurveyResponseCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.SurveyResponse;
import pe.edu.upc.soft.work.platform.feedback.domain.model.valueObjects.EmployeeProfileId;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.CreateSurveyResponseRequest;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.SurveyResponseResponse;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.UpdateSurveyResponseRequest;

public class SurveyResponseAssembler {

    /**
     *  Converts a CreateSurveyResponseRequest to a CreateSurveyResponseCommand.
     * */
    public static CreateSurveyResponseCommand toCommandFromRequest(CreateSurveyResponseRequest request){
        return new CreateSurveyResponseCommand(request.surveyId(),new EmployeeProfileId(request.employeeProfileId()), request.submittedAt());
    }

    /**
     *  Converts an UpdateSurveyResponseRequest to an UpdateSurveyResponseCommand.
     */
    public static UpdateSurveyResponseCommand toCommandFromRequest(Long surveyResponseId, UpdateSurveyResponseRequest request){
        return new UpdateSurveyResponseCommand(surveyResponseId, request.surveyId(), new EmployeeProfileId(request.employeeProfileId()), request.submittedAt());
    }

    /**
     *  Converts a SurveyResponse entity to a SurveyResponseResponse.
     */
    public static SurveyResponseResponse toResponseFromEntity(SurveyResponse response){
        return new SurveyResponseResponse(response.getId(), response.getSurveyId(), response.getEmployeeProfileId().employeeProfileId(), response.getSubmittedAt());
    }
}
