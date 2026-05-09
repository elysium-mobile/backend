package pe.edu.upc.soft.work.platform.feedback.domain.model.aggregates;

import jakarta.persistence.Entity;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.CreateSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.UpdateSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.valueObjects.TargetType;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import java.util.Date;

/**
 * Survey aggregate root entity.
 */
@Entity
public class Survey extends AuditableAbstractAggregateRoot<Survey> {

    @Getter
    private String title;
    @Getter
    private String description;
    @Getter
    private TargetType targetType;
    @Getter
    private Date expirationTime;

    /**
     * Default constructor for JPA.
     */
    public Survey() {}

    /**
     * Constructor to create a Survey from a CreateSurveyCommand.
     * @param command the command containing survey details
     */
    public Survey(CreateSurveyCommand command) {
        this.title = command.title();
        this.description = command.description();
        this.targetType = command.targetType();
        this.expirationTime = command.expirationTime();
    }

    /**
     * Updates the Survey with details from an UpdateSurveyCommand.
     * @param command the command containing updated survey details
     */
    public void updateSurvey(UpdateSurveyCommand command) {
        this.title = command.title();
        this.description = command.description();
        this.targetType = command.targetType();
        this.expirationTime = command.expirationTime();
    }
}
