package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources;

import java.util.Date;

public record SurveyResponseResponse(

        Long surveyResponseId,
        Long surveyId,
        Long employeeProfileId,
        Date SubmittedAt,
        String commentary,
        String cause
) {
}
