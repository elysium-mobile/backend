package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import java.util.Date;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record SurveyResponseResponse(

        Long surveyResponseId,
        Long surveyId,
        Long employeeProfileId,
        Date SubmittedAt,
        String commentary,
        String cause
) {
}
