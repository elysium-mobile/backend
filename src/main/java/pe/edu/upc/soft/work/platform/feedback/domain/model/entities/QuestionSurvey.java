package pe.edu.upc.soft.work.platform.feedback.domain.model.entities;

import jakarta.persistence.Entity;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.CreateQuestionSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.UpdateQuestionSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.valueObjects.QuestionType;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

/**
 * QuestionSurvey aggregate root entity.
 */
@Entity
public class QuestionSurvey extends AuditableAbstractAggregateRoot<QuestionSurvey> {

    @Getter
    private String textQuestion;
    @Getter
    private QuestionType questionType;

    /**
     * Default constructor for JPA.
     */
    public QuestionSurvey() {}

    /**
     * Constructor to create a QuestionSurvey from a CreateQuestionSurveyCommand.
     * @param command the command containing questionsurvey details
     */
    public QuestionSurvey(CreateQuestionSurveyCommand command) {
        this.textQuestion = command.textQuestion();
        this.questionType = command.questionType();
    }

    /**
     * Updates the QuestionSurvey with details from an UpdateQuestionSurveyCommand.
     * @param command the command containing updated questionsurvey details
     */
    public void updateQuestionSurvey(UpdateQuestionSurveyCommand command) {
        this.textQuestion = command.textQuestion();
        this.questionType = command.questionType();
    }
}
