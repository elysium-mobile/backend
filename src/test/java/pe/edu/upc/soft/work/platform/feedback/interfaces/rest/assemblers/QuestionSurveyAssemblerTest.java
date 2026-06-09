package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.assemblers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.CreateQuestionSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.UpdateQuestionSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.QuestionSurvey;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.CreateQuestionSurveyRequest;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.QuestionSurveyResponse;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.UpdateQuestionSurveyRequest;
import pe.edu.upc.soft.work.platform.feedback.test.fixtures.FeedbackCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class QuestionSurveyAssemblerTest {

    @Test
    @DisplayName("toCommandFromRequest(CreateQuestionSurveyRequest) -> maps fields and resolves QuestionType enum (AAA)")
    void toCommandFromCreateRequestMapsAllFields() {
        // Arrange
        var request = new CreateQuestionSurveyRequest(
                FeedbackCommandFixtures.VALID_QUESTION_TEXT,
                FeedbackCommandFixtures.VALID_QUESTION_TYPE.name(),
            FeedbackCommandFixtures.VALID_SURVEY_ID);

        // Act
        CreateQuestionSurveyCommand command = QuestionSurveyAssembler.toCommandFromRequest(request);

        // Assert
        assertThat(command.textQuestion()).isEqualTo(FeedbackCommandFixtures.VALID_QUESTION_TEXT);
        assertThat(command.questionType()).isEqualTo(FeedbackCommandFixtures.VALID_QUESTION_TYPE);
        assertThat(command.surveyId()).isEqualTo(FeedbackCommandFixtures.VALID_SURVEY_ID);
    }

    @Test
    @DisplayName("toCommandFromRequest(Long, UpdateQuestionSurveyRequest) -> maps id and fields to UpdateQuestionSurveyCommand (AAA)")
    void toCommandFromUpdateRequestMapsAllFields() {
        // Arrange
        var request = new UpdateQuestionSurveyRequest(
                FeedbackCommandFixtures.VALID_QUESTION_TEXT,
                FeedbackCommandFixtures.VALID_QUESTION_TYPE.name(),
            FeedbackCommandFixtures.VALID_SURVEY_ID);

        // Act
        UpdateQuestionSurveyCommand command = QuestionSurveyAssembler.toCommandFromRequest(14L, request);

        // Assert
        assertThat(command.questionSurveyId()).isEqualTo(14L);
        assertThat(command.textQuestion()).isEqualTo(FeedbackCommandFixtures.VALID_QUESTION_TEXT);
        assertThat(command.questionType()).isEqualTo(FeedbackCommandFixtures.VALID_QUESTION_TYPE);
        assertThat(command.surveyId()).isEqualTo(FeedbackCommandFixtures.VALID_SURVEY_ID);
    }

    @Test
    @DisplayName("toResponseFromEntity(QuestionSurvey) -> maps id, text and serializes QuestionType as name() (AAA)")
    void toResponseFromEntityMapsAllFields() {
        // Arrange
        var entity = new QuestionSurvey(FeedbackCommandFixtures.validCreateQuestionSurveyCommand());
        ReflectionTestUtils.setId(entity, 14L);

        // Act
        QuestionSurveyResponse response = QuestionSurveyAssembler.toResponseFromEntity(entity);

        // Assert
        assertThat(response.questionSurveyId()).isEqualTo(14L);
        assertThat(response.textQuestion()).isEqualTo(FeedbackCommandFixtures.VALID_QUESTION_TEXT);
        assertThat(response.questionType()).isEqualTo(FeedbackCommandFixtures.VALID_QUESTION_TYPE.name());
        assertThat(response.surveyId()).isEqualTo(FeedbackCommandFixtures.VALID_SURVEY_ID);
    }
}
