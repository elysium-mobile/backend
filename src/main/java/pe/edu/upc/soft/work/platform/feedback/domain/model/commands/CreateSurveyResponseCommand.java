package pe.edu.upc.soft.work.platform.feedback.domain.model.commands;

import pe.edu.upc.soft.work.platform.feedback.domain.model.valueObjects.EmployeeProfileId;

import java.util.Date;
import java.util.Objects;

/**
 * Command to create a new SurveyResponse
 */
public record CreateSurveyResponseCommand(Long surveyId, EmployeeProfileId employeeProfileId, Date submittedAt, String commentary, String cause) {

    /**
     * Constructor with validation
     */
    public CreateSurveyResponseCommand {
        Objects.requireNonNull(surveyId, "[CreateSurveyResponseCommand] surveyId must not be null");
        Objects.requireNonNull(employeeProfileId, "[CreateSurveyResponseCommand] employeeProfileId must not be null");
        Objects.requireNonNull(submittedAt, "[CreateSurveyResponseCommand] submittedAt must not be null");
    }
}
