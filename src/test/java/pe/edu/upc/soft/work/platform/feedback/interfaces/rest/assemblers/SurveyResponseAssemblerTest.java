package pe.edu.upc.soft.work.platform.feedback.interfaces.rest.assemblers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.CreateSurveyResponseCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.UpdateSurveyResponseCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.SurveyResponse;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.CreateSurveyResponseRequest;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.SurveyResponseResponse;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.UpdateSurveyResponseRequest;
import pe.edu.upc.soft.work.platform.feedback.test.fixtures.FeedbackCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class SurveyResponseAssemblerTest {

    @Test
    @DisplayName("toCommandFromRequest(CreateSurveyResponseRequest) -> maps fields and wraps employeeProfileId in VO (AAA)")
    void toCommandFromCreateRequestMapsAllFields() {
        // Arrange
        var request = new CreateSurveyResponseRequest(
                FeedbackCommandFixtures.VALID_SURVEY_ID,
                FeedbackCommandFixtures.VALID_EMPLOYEE_PROFILE_ID,
                FeedbackCommandFixtures.VALID_SUBMITTED_AT);

        // Act
        CreateSurveyResponseCommand command = SurveyResponseAssembler.toCommandFromRequest(request);

        // Assert
        assertThat(command.surveyId()).isEqualTo(FeedbackCommandFixtures.VALID_SURVEY_ID);
        assertThat(command.employeeProfileId().employeeProfileId())
                .isEqualTo(FeedbackCommandFixtures.VALID_EMPLOYEE_PROFILE_ID);
        assertThat(command.submittedAt()).isEqualTo(FeedbackCommandFixtures.VALID_SUBMITTED_AT);
    }

    @Test
    @DisplayName("toCommandFromRequest(Long, UpdateSurveyResponseRequest) -> maps id, surveyId, employeeProfileId VO and submittedAt (AAA)")
    void toCommandFromUpdateRequestMapsAllFields() {
        // Arrange
        var request = new UpdateSurveyResponseRequest(
                FeedbackCommandFixtures.VALID_SURVEY_ID,
                FeedbackCommandFixtures.VALID_EMPLOYEE_PROFILE_ID,
                FeedbackCommandFixtures.VALID_SUBMITTED_AT);

        // Act
        UpdateSurveyResponseCommand command = SurveyResponseAssembler.toCommandFromRequest(31L, request);

        // Assert
        assertThat(command.surveyresponseId()).isEqualTo(31L);
        assertThat(command.surveyId()).isEqualTo(FeedbackCommandFixtures.VALID_SURVEY_ID);
        assertThat(command.employeeProfileId().employeeProfileId())
                .isEqualTo(FeedbackCommandFixtures.VALID_EMPLOYEE_PROFILE_ID);
        assertThat(command.submittedAt()).isEqualTo(FeedbackCommandFixtures.VALID_SUBMITTED_AT);
    }

    @Test
    @DisplayName("toResponseFromEntity(SurveyResponse) -> unwraps employeeProfileId VO and maps all fields (AAA)")
    void toResponseFromEntityMapsAllFields() {
        // Arrange
        var entity = new SurveyResponse(FeedbackCommandFixtures.validCreateSurveyResponseCommand());
        ReflectionTestUtils.setId(entity, 31L);

        // Act
        SurveyResponseResponse response = SurveyResponseAssembler.toResponseFromEntity(entity);

        // Assert
        assertThat(response.surveyResponseId()).isEqualTo(31L);
        assertThat(response.surveyId()).isEqualTo(FeedbackCommandFixtures.VALID_SURVEY_ID);
        assertThat(response.employeeProfileId()).isEqualTo(FeedbackCommandFixtures.VALID_EMPLOYEE_PROFILE_ID);
        assertThat(response.SubmittedAt()).isEqualTo(FeedbackCommandFixtures.VALID_SUBMITTED_AT);
    }
}
