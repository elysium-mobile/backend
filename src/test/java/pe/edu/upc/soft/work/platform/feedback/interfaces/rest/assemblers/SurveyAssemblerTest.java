package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.assemblers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.soft.work.platform.feedback.domain.model.aggregates.Survey;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.CreateSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.UpdateSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.CreateSurveyRequest;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.SurveyResponse;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.UpdateSurveyRequest;
import pe.edu.upc.soft.work.platform.feedback.test.fixtures.FeedbackCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class SurveyAssemblerTest {

    @Test
    @DisplayName("toCommandFromRequest(CreateSurveyRequest) -> maps all fields and resolves TargetType enum (AAA)")
    void toCommandFromCreateRequestMapsAllFields() {
        // Arrange
        var request = new CreateSurveyRequest(
                FeedbackCommandFixtures.VALID_SURVEY_TITLE,
                FeedbackCommandFixtures.VALID_SURVEY_DESCRIPTION,
                FeedbackCommandFixtures.VALID_TARGET_TYPE.name(),
                FeedbackCommandFixtures.VALID_EXPIRATION_TIME);

        // Act
        CreateSurveyCommand command = SurveyAssembler.toCommandFromRequest(request);

        // Assert
        assertThat(command.title()).isEqualTo(FeedbackCommandFixtures.VALID_SURVEY_TITLE);
        assertThat(command.description()).isEqualTo(FeedbackCommandFixtures.VALID_SURVEY_DESCRIPTION);
        assertThat(command.targetType()).isEqualTo(FeedbackCommandFixtures.VALID_TARGET_TYPE);
        assertThat(command.expirationTime()).isEqualTo(FeedbackCommandFixtures.VALID_EXPIRATION_TIME);
    }

    @Test
    @DisplayName("toCommandFromRequest(Long, UpdateSurveyRequest) -> maps id and all fields to UpdateSurveyCommand (AAA)")
    void toCommandFromUpdateRequestMapsAllFields() {
        // Arrange
        var request = new UpdateSurveyRequest(
                FeedbackCommandFixtures.VALID_SURVEY_TITLE,
                FeedbackCommandFixtures.VALID_SURVEY_DESCRIPTION,
                FeedbackCommandFixtures.VALID_TARGET_TYPE.name(),
                FeedbackCommandFixtures.VALID_EXPIRATION_TIME);

        // Act
        UpdateSurveyCommand command = SurveyAssembler.toCommandFromRequest(7L, request);

        // Assert
        assertThat(command.surveyId()).isEqualTo(7L);
        assertThat(command.title()).isEqualTo(FeedbackCommandFixtures.VALID_SURVEY_TITLE);
        assertThat(command.description()).isEqualTo(FeedbackCommandFixtures.VALID_SURVEY_DESCRIPTION);
        assertThat(command.targetType()).isEqualTo(FeedbackCommandFixtures.VALID_TARGET_TYPE);
        assertThat(command.expirationTime()).isEqualTo(FeedbackCommandFixtures.VALID_EXPIRATION_TIME);
    }

    @Test
    @DisplayName("toResponseFromEntity(Survey) -> maps every field and serializes TargetType as name() (AAA)")
    void toResponseFromEntityMapsAllFields() {
        // Arrange
        var survey = new Survey(FeedbackCommandFixtures.validCreateSurveyCommand());
        ReflectionTestUtils.setId(survey, 7L);

        // Act
        SurveyResponse response = SurveyAssembler.toResponseFromEntity(survey);

        // Assert
        assertThat(response.surveyId()).isEqualTo(7L);
        assertThat(response.title()).isEqualTo(FeedbackCommandFixtures.VALID_SURVEY_TITLE);
        assertThat(response.description()).isEqualTo(FeedbackCommandFixtures.VALID_SURVEY_DESCRIPTION);
        assertThat(response.targetType()).isEqualTo(FeedbackCommandFixtures.VALID_TARGET_TYPE.name());
        assertThat(response.expirationTime()).isEqualTo(FeedbackCommandFixtures.VALID_EXPIRATION_TIME);
    }
}
