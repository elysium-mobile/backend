package pe.edu.upc.soft.work.platform.feedback.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.feedback.application.internal.outboundservices.acl.ExternalIamServiceFromFeedback;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.DeleteSurveyResponseCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.SurveyResponse;
import pe.edu.upc.soft.work.platform.feedback.infrastructure.persistence.jpa.repositories.SurveyRepository;
import pe.edu.upc.soft.work.platform.feedback.infrastructure.persistence.jpa.repositories.SurveyResponseRepository;
import pe.edu.upc.soft.work.platform.feedback.test.fixtures.FeedbackCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SurveyResponseCommandServiceImplTest {

    private static final Long RESPONSE_ID = 31L;

    @Mock
    private SurveyResponseRepository surveyResponseRepository;
    @Mock
    private ExternalIamServiceFromFeedback externalIamServiceFromFeedback;
    @Mock
    private SurveyRepository surveyRepository;

    @InjectMocks
    private SurveyResponseCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateSurveyResponseCommand) -> creates SurveyResponse when survey and employee profile exist (AAA)")
    void handleCreateSuccess() {
        // Arrange
        var command = FeedbackCommandFixtures.validCreateSurveyResponseCommand();
        when(surveyRepository.existsById(FeedbackCommandFixtures.VALID_SURVEY_ID)).thenReturn(true);
        when(externalIamServiceFromFeedback.existEmployeeProfileById(FeedbackCommandFixtures.VALID_EMPLOYEE_PROFILE_ID))
                .thenReturn(true);
        when(surveyResponseRepository.save(any(SurveyResponse.class))).thenAnswer(inv -> {
            SurveyResponse sr = inv.getArgument(0);
            ReflectionTestUtils.setId(sr, RESPONSE_ID);
            return sr;
        });

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(RESPONSE_ID);
        verify(surveyRepository, times(1)).existsById(FeedbackCommandFixtures.VALID_SURVEY_ID);
        verify(externalIamServiceFromFeedback, times(1))
                .existEmployeeProfileById(FeedbackCommandFixtures.VALID_EMPLOYEE_PROFILE_ID);
        verify(surveyResponseRepository, times(1)).save(any(SurveyResponse.class));
        verifyNoMoreInteractions(surveyRepository, externalIamServiceFromFeedback, surveyResponseRepository);
    }

    @Test
    @DisplayName("handle(CreateSurveyResponseCommand) -> throws NotFoundArgumentException when survey is missing (AAA)")
    void handleCreateMissingSurvey() {
        // Arrange
        var command = FeedbackCommandFixtures.validCreateSurveyResponseCommand();
        when(surveyRepository.existsById(FeedbackCommandFixtures.VALID_SURVEY_ID)).thenReturn(false);

        // Act + Assert
        NotFoundArgumentException ex = assertThrows(NotFoundArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Survey ID: " + FeedbackCommandFixtures.VALID_SURVEY_ID);
        verify(surveyRepository, times(1)).existsById(FeedbackCommandFixtures.VALID_SURVEY_ID);
        verifyNoMoreInteractions(surveyRepository);
        verifyNoInteractions(externalIamServiceFromFeedback, surveyResponseRepository);
    }

    @Test
    @DisplayName("handle(CreateSurveyResponseCommand) -> throws NotFoundArgumentException when employee profile is missing (AAA)")
    void handleCreateMissingEmployeeProfile() {
        // Arrange
        var command = FeedbackCommandFixtures.validCreateSurveyResponseCommand();
        when(surveyRepository.existsById(FeedbackCommandFixtures.VALID_SURVEY_ID)).thenReturn(true);
        when(externalIamServiceFromFeedback.existEmployeeProfileById(FeedbackCommandFixtures.VALID_EMPLOYEE_PROFILE_ID))
                .thenReturn(false);

        // Act + Assert
        NotFoundArgumentException ex = assertThrows(NotFoundArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Employee Profile ID: " + FeedbackCommandFixtures.VALID_EMPLOYEE_PROFILE_ID);
        verify(surveyRepository, times(1)).existsById(FeedbackCommandFixtures.VALID_SURVEY_ID);
        verify(externalIamServiceFromFeedback, times(1))
                .existEmployeeProfileById(FeedbackCommandFixtures.VALID_EMPLOYEE_PROFILE_ID);
        verifyNoMoreInteractions(surveyRepository, externalIamServiceFromFeedback);
        verifyNoInteractions(surveyResponseRepository);
    }

    @Test
    @DisplayName("handle(CreateSurveyResponseCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleCreateSaveFailure() {
        // Arrange
        var command = FeedbackCommandFixtures.validCreateSurveyResponseCommand();
        when(surveyRepository.existsById(FeedbackCommandFixtures.VALID_SURVEY_ID)).thenReturn(true);
        when(externalIamServiceFromFeedback.existEmployeeProfileById(FeedbackCommandFixtures.VALID_EMPLOYEE_PROFILE_ID))
                .thenReturn(true);
        when(surveyResponseRepository.save(any(SurveyResponse.class))).thenThrow(new RuntimeException("db"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error creating SurveyResponse").contains("db");
        verify(surveyRepository, times(1)).existsById(FeedbackCommandFixtures.VALID_SURVEY_ID);
        verify(externalIamServiceFromFeedback, times(1))
                .existEmployeeProfileById(FeedbackCommandFixtures.VALID_EMPLOYEE_PROFILE_ID);
        verify(surveyResponseRepository, times(1)).save(any(SurveyResponse.class));
        verifyNoMoreInteractions(surveyRepository, externalIamServiceFromFeedback, surveyResponseRepository);
    }

    @Test
    @DisplayName("handle(UpdateSurveyResponseCommand) -> returns Optional with updated SurveyResponse when present (AAA)")
    void handleUpdateSuccess() {
        // Arrange
        var existing = new SurveyResponse(FeedbackCommandFixtures.validCreateSurveyResponseCommand());
        ReflectionTestUtils.setId(existing, RESPONSE_ID);
        var command = FeedbackCommandFixtures.updateSurveyResponseCommand(RESPONSE_ID);
        when(surveyResponseRepository.existsById(RESPONSE_ID)).thenReturn(true);
        when(surveyResponseRepository.findById(RESPONSE_ID)).thenReturn(Optional.of(existing));
        when(surveyResponseRepository.save(existing)).thenReturn(existing);

        // Act
        Optional<SurveyResponse> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getSurveyId()).isEqualTo(FeedbackCommandFixtures.VALID_SURVEY_ID);
        assertThat(result.get().getEmployeeProfileId().employeeProfileId())
                .isEqualTo(FeedbackCommandFixtures.VALID_EMPLOYEE_PROFILE_ID);
        verify(surveyResponseRepository, times(1)).existsById(RESPONSE_ID);
        verify(surveyResponseRepository, times(1)).findById(RESPONSE_ID);
        verify(surveyResponseRepository, times(1)).save(existing);
        verifyNoMoreInteractions(surveyResponseRepository);
        verifyNoInteractions(surveyRepository, externalIamServiceFromFeedback);
    }

    @Test
    @DisplayName("handle(UpdateSurveyResponseCommand) -> throws RuntimeException when id does not exist (AAA)")
    void handleUpdateMissing() {
        // Arrange
        var command = FeedbackCommandFixtures.updateSurveyResponseCommand(RESPONSE_ID);
        when(surveyResponseRepository.existsById(RESPONSE_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(RESPONSE_ID)).contains("does not exist");
        verify(surveyResponseRepository, times(1)).existsById(RESPONSE_ID);
        verifyNoMoreInteractions(surveyResponseRepository);
        verifyNoInteractions(surveyRepository, externalIamServiceFromFeedback);
    }

    @Test
    @DisplayName("handle(UpdateSurveyResponseCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleUpdateSaveFailure() {
        // Arrange
        var existing = new SurveyResponse(FeedbackCommandFixtures.validCreateSurveyResponseCommand());
        ReflectionTestUtils.setId(existing, RESPONSE_ID);
        var command = FeedbackCommandFixtures.updateSurveyResponseCommand(RESPONSE_ID);
        when(surveyResponseRepository.existsById(RESPONSE_ID)).thenReturn(true);
        when(surveyResponseRepository.findById(RESPONSE_ID)).thenReturn(Optional.of(existing));
        when(surveyResponseRepository.save(existing)).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error updating SurveyResponse").contains("boom");
        verify(surveyResponseRepository, times(1)).existsById(RESPONSE_ID);
        verify(surveyResponseRepository, times(1)).findById(RESPONSE_ID);
        verify(surveyResponseRepository, times(1)).save(existing);
        verifyNoMoreInteractions(surveyResponseRepository);
        verifyNoInteractions(surveyRepository, externalIamServiceFromFeedback);
    }

    @Test
    @DisplayName("handle(DeleteSurveyResponseCommand) -> deletes when present (AAA)")
    void handleDeleteSuccess() {
        // Arrange
        var command = new DeleteSurveyResponseCommand(RESPONSE_ID);
        when(surveyResponseRepository.existsById(RESPONSE_ID)).thenReturn(true);

        // Act
        service.handle(command);

        // Assert
        verify(surveyResponseRepository, times(1)).existsById(RESPONSE_ID);
        verify(surveyResponseRepository, times(1)).deleteById(RESPONSE_ID);
        verifyNoMoreInteractions(surveyResponseRepository);
        verifyNoInteractions(surveyRepository, externalIamServiceFromFeedback);
    }

    @Test
    @DisplayName("handle(DeleteSurveyResponseCommand) -> throws RuntimeException when id is absent (AAA)")
    void handleDeleteMissing() {
        // Arrange
        var command = new DeleteSurveyResponseCommand(RESPONSE_ID);
        when(surveyResponseRepository.existsById(RESPONSE_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(RESPONSE_ID)).contains("does not exist");
        verify(surveyResponseRepository, times(1)).existsById(RESPONSE_ID);
        verify(surveyResponseRepository, never()).deleteById(any(Long.class));
        verifyNoMoreInteractions(surveyResponseRepository);
        verifyNoInteractions(surveyRepository, externalIamServiceFromFeedback);
    }

    @Test
    @DisplayName("handle(DeleteSurveyResponseCommand) -> wraps deleteById failure in RuntimeException (AAA)")
    void handleDeleteDeleteFailure() {
        // Arrange
        var command = new DeleteSurveyResponseCommand(RESPONSE_ID);
        when(surveyResponseRepository.existsById(RESPONSE_ID)).thenReturn(true);
        doThrow(new RuntimeException("fk")).when(surveyResponseRepository).deleteById(RESPONSE_ID);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error deleting SurveyResponse").contains("fk");
        verify(surveyResponseRepository, times(1)).existsById(RESPONSE_ID);
        verify(surveyResponseRepository, times(1)).deleteById(RESPONSE_ID);
        verifyNoMoreInteractions(surveyResponseRepository);
        verifyNoInteractions(surveyRepository, externalIamServiceFromFeedback);
    }
}
