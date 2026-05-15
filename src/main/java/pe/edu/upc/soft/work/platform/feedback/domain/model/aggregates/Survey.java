package pe.edu.upc.soft.work.platform.feedback.domain.model.aggregates;

import jakarta.persistence.*;
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
@Table(name = "surveys")
public class Survey extends AuditableAbstractAggregateRoot<Survey> {

    @Getter
    @Column(name ="title", nullable = false)
    private String title;
    @Getter
    @Column(name ="description", nullable = false)
    private String description;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name ="target_type", nullable = false)
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
