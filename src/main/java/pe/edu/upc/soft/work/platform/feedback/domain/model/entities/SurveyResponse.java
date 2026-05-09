package pe.edu.upc.soft.work.platform.feedback.domain.model.entities;

import jakarta.persistence.Entity;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.CreateSurveyResponseCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.UpdateSurveyResponseCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.valueObjects.EmployeeProfileId;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import java.util.Date;

/**
 * SurveyResponse aggregate root entity.
 */
@Entity
public class SurveyResponse extends AuditableAbstractAggregateRoot<SurveyResponse> {

    @Getter
    private Long surveyId;
    @Getter
    private EmployeeProfileId employeeProfileId;
    @Getter
    private Date submittedAt;

    /**
     * Default constructor for JPA.
     */
    public SurveyResponse() {}

    /**
     * Constructor to create a SurveyResponse from a CreateSurveyResponseCommand.
     * @param command the command containing surveyresponse details
     */
    public SurveyResponse(CreateSurveyResponseCommand command) {
        this.surveyId = command.surveyId();
        this.employeeProfileId = command.employeeProfileId();
        this.submittedAt = command.submittedAt();
    }

    /**
     * Updates the SurveyResponse with details from an UpdateSurveyResponseCommand.
     * @param command the command containing updated surveyresponse details
     */
    public void updateSurveyResponse(UpdateSurveyResponseCommand command) {
        this.surveyId = command.surveyId();
        this.employeeProfileId = command.employeeProfileId();
        this.submittedAt = command.submittedAt();
    }
}
