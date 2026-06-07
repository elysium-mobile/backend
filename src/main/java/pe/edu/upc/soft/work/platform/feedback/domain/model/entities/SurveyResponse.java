package pe.edu.upc.soft.work.platform.feedback.domain.model.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
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
@Table(name = "survey_responses")
public class SurveyResponse extends AuditableAbstractAggregateRoot<SurveyResponse> {

    @Getter
    @Column(name = "survey_id", nullable = false)
    private Long surveyId;

    @Getter
    @Embedded
    @AttributeOverride(name = "employeeProfileId", column = @Column(name = "employee_profile_id"))
    @JsonProperty("employeeProfileId")
    private EmployeeProfileId employeeProfileId;

    @Getter
    @Column(name = "submitted_at")
    private Date submittedAt;

    @Getter
    @Column(name = "commentary", nullable = false)
    private String commentary;

    @Getter
    @Column(name = "cause", nullable = false)
    private String cause;

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
        this.commentary = command.commentary();
        this.cause = command.cause();
    }

    /**
     * Updates the SurveyResponse with details from an UpdateSurveyResponseCommand.
     * @param command the command containing updated surveyresponse details
     */
    public void updateSurveyResponse(UpdateSurveyResponseCommand command) {
        this.surveyId = command.surveyId();
        this.employeeProfileId = command.employeeProfileId();
        this.submittedAt = command.submittedAt();
        this.commentary = command.commentary();
        this.cause = command.cause();
    }
}
