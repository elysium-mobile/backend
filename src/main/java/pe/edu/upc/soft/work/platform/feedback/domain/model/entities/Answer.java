package pe.edu.upc.soft.work.platform.feedback.domain.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.CreateAnswerCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.UpdateAnswerCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;


/**
 * Answer aggregate root entity.
 */
@Entity
@Table(name = "answers")
public class Answer extends AuditableAbstractAggregateRoot<Answer> {

    @Getter
    @Column(name = "value", nullable = false)
    private Long value;

    @Getter
    @Column(name = "score_answer", nullable = false)
    private Integer scoreAnswer;

    /**
     * Default constructor for JPA.
     */
    public Answer() {}

    /**
     * Constructor to create a Answer from a CreateAnswerCommand.
     * @param command the command containing answer details
     */
    public Answer(CreateAnswerCommand command) {
        this.value = command.value();
        this.scoreAnswer = command.scoreAnswer();
    }

    /**
     * Updates the Answer with details from an UpdateAnswerCommand.
     * @param command the command containing updated answer details
     */
    public void updateAnswer(UpdateAnswerCommand command) {
        this.value = command.value();
        this.scoreAnswer = command.scoreAnswer();
    }
}
