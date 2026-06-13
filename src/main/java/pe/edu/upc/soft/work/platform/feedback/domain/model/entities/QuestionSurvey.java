package pe.edu.upc.soft.work.platform.feedback.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.CreateQuestionSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.UpdateQuestionSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.valueObjects.QuestionType;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

/**
 * QuestionSurvey aggregate root entity.
 */
@Entity
@Table(name = "questions_survey")
public class QuestionSurvey extends AuditableAbstractAggregateRoot<QuestionSurvey> {

    @Getter
    @Column(nullable = false)
    private String textQuestion;


    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    private QuestionType questionType;

    @Getter
    @Column(name = "survey_id", nullable = false)
    private Long surveyId;

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
        this.surveyId = command.surveyId();
    }

    /**
     * Updates the QuestionSurvey with details from an UpdateQuestionSurveyCommand.
     * @param command the command containing updated questionsurvey details
     */
    public void updateQuestionSurvey(UpdateQuestionSurveyCommand command) {
        this.textQuestion = command.textQuestion();
        this.questionType = command.questionType();
        this.surveyId = command.surveyId();
    }
}
