package pe.edu.upc.soft.work.platform.feedback.test.fixtures;

import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.CreateAnswerCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.CreateQuestionSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.CreateSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.CreateSurveyResponseCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.UpdateAnswerCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.UpdateQuestionSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.UpdateSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.UpdateSurveyResponseCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.valueObjects.EmployeeProfileId;
import pe.edu.upc.soft.work.platform.feedback.domain.model.valueObjects.QuestionType;
import pe.edu.upc.soft.work.platform.feedback.domain.model.valueObjects.TargetType;

import java.util.Date;

/**
 * Feedback-specific command factories. Mirrors {@code IamCommandFixtures}
 * and {@code DashboardCommandFixtures}: every static method returns a
 * fresh, valid command instance from canonical constants. Tests MUST NOT
 * instantiate feedback commands inline.
 */
public final class FeedbackCommandFixtures {

    public static final String VALID_SURVEY_TITLE = "Q1 Engagement Survey";
    public static final String VALID_SURVEY_DESCRIPTION = "Quarterly engagement pulse";
    public static final TargetType VALID_TARGET_TYPE = TargetType.UNIT_OF_WORK;
    public static final Date VALID_EXPIRATION_TIME = new Date(1_700_000_000_000L);

    public static final String VALID_QUESTION_TEXT = "How satisfied are you?";
    public static final QuestionType VALID_QUESTION_TYPE = QuestionType.RATING;

    public static final Long VALID_ANSWER_VALUE = 5L;
    public static final Integer VALID_ANSWER_SCORE = 100;

    public static final Long VALID_SURVEY_ID = 1L;
    public static final Long VALID_EMPLOYEE_PROFILE_ID = 10L;
    public static final Date VALID_SUBMITTED_AT = new Date(1_700_500_000_000L);

    public static final String VALID_COMMENTARY ="Generic commentary";
    public static final String VALID_CAUSE ="Generic cause";


    private FeedbackCommandFixtures() {
        throw new AssertionError("FeedbackCommandFixtures is a utility class and must not be instantiated.");
    }

    // ---------- Survey ----------
    public static CreateSurveyCommand validCreateSurveyCommand() {
        return new CreateSurveyCommand(
                VALID_SURVEY_TITLE, VALID_SURVEY_DESCRIPTION, VALID_TARGET_TYPE, VALID_EXPIRATION_TIME);
    }

    public static UpdateSurveyCommand updateSurveyCommand(Long surveyId) {
        return new UpdateSurveyCommand(
                surveyId, VALID_SURVEY_TITLE, VALID_SURVEY_DESCRIPTION, VALID_TARGET_TYPE, VALID_EXPIRATION_TIME);
    }

    // ---------- QuestionSurvey ----------
    public static CreateQuestionSurveyCommand validCreateQuestionSurveyCommand() {
        return new CreateQuestionSurveyCommand(VALID_QUESTION_TEXT, VALID_QUESTION_TYPE,VALID_SURVEY_ID);
    }

    public static UpdateQuestionSurveyCommand updateQuestionSurveyCommand(Long questionSurveyId) {
        return new UpdateQuestionSurveyCommand(questionSurveyId, VALID_QUESTION_TEXT, VALID_QUESTION_TYPE,VALID_SURVEY_ID);
    }

    // ---------- Answer ----------
    public static CreateAnswerCommand validCreateAnswerCommand() {
        return new CreateAnswerCommand(VALID_ANSWER_VALUE, VALID_ANSWER_SCORE);
    }

    public static UpdateAnswerCommand updateAnswerCommand(Long answerId) {
        return new UpdateAnswerCommand(answerId, VALID_ANSWER_VALUE, VALID_ANSWER_SCORE);
    }

    // ---------- SurveyResponse ----------
    public static CreateSurveyResponseCommand validCreateSurveyResponseCommand() {
        return new CreateSurveyResponseCommand(
                VALID_SURVEY_ID,
                new EmployeeProfileId(VALID_EMPLOYEE_PROFILE_ID),
                VALID_SUBMITTED_AT,null,null);
    }

    public static UpdateSurveyResponseCommand updateSurveyResponseCommand(Long surveyResponseId) {
        return new UpdateSurveyResponseCommand(
                surveyResponseId,
                VALID_SURVEY_ID,
                new EmployeeProfileId(VALID_EMPLOYEE_PROFILE_ID),
                VALID_SUBMITTED_AT,
            null,null);
    }
}
