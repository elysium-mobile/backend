package pe.edu.upc.soft.work.platform.feedback.domain.model.commands;

import pe.edu.upc.soft.work.platform.feedback.domain.model.valueObjects.EmployeeProfileId;

import java.util.Date;
import java.util.Objects;

/**
 * Command to update an existing SurveyResponse
 */
public record UpdateSurveyResponseCommand(Long surveyresponseId, Long surveyId, EmployeeProfileId employeeProfileId, Date submittedAt, String commentary, String cause) {

    /**
     * Constructor with validation
     */
    public UpdateSurveyResponseCommand {
        Objects.requireNonNull(surveyresponseId, "[UpdateSurveyResponseCommand] surveyresponseId must not be null");
    }
}
