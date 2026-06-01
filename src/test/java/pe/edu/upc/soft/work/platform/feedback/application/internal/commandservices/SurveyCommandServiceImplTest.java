package pe.edu.upc.soft.work.platform.feedback.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.feedback.domain.model.aggregates.Survey;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.DeleteSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.infrastructure.persistence.jpa.repositories.SurveyRepository;
import pe.edu.upc.soft.work.platform.feedback.test.fixtures.FeedbackCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SurveyCommandServiceImplTest {

    private static final Long SURVEY_ID = 7L;

    @Mock
    private SurveyRepository surveyRepository;

    @InjectMocks
    private SurveyCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateSurveyCommand) -> creates Survey and returns generated id (AAA)")
    void handleCreateSuccess() {
        // Arrange
        var command = FeedbackCommandFixtures.validCreateSurveyCommand();
        when(surveyRepository.save(any(Survey.class))).thenAnswer(inv -> {
            Survey s = inv.getArgument(0);
            ReflectionTestUtils.setId(s, SURVEY_ID);
            return s;
        });

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(SURVEY_ID);
        verify(surveyRepository, times(1)).save(any(Survey.class));
        verifyNoMoreInteractions(surveyRepository);
    }

    @Test
    @DisplayName("handle(CreateSurveyCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleCreateSaveFailure() {
        // Arrange
        var command = FeedbackCommandFixtures.validCreateSurveyCommand();
        when(surveyRepository.save(any(Survey.class))).thenThrow(new RuntimeException("db"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error creating Survey").contains("db");
        verify(surveyRepository, times(1)).save(any(Survey.class));
        verifyNoMoreInteractions(surveyRepository);
    }

    @Test
    @DisplayName("handle(UpdateSurveyCommand) -> returns Optional with updated Survey when present (AAA)")
    void handleUpdateSuccess() {
        // Arrange
        var existing = new Survey(FeedbackCommandFixtures.validCreateSurveyCommand());
        ReflectionTestUtils.setId(existing, SURVEY_ID);
        var command = FeedbackCommandFixtures.updateSurveyCommand(SURVEY_ID);
        when(surveyRepository.existsById(SURVEY_ID)).thenReturn(true);
        when(surveyRepository.findById(SURVEY_ID)).thenReturn(Optional.of(existing));
        when(surveyRepository.save(existing)).thenReturn(existing);

        // Act
        Optional<Survey> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo(FeedbackCommandFixtures.VALID_SURVEY_TITLE);
        assertThat(result.get().getTargetType()).isEqualTo(FeedbackCommandFixtures.VALID_TARGET_TYPE);
        verify(surveyRepository, times(1)).existsById(SURVEY_ID);
        verify(surveyRepository, times(1)).findById(SURVEY_ID);
        verify(surveyRepository, times(1)).save(existing);
        verifyNoMoreInteractions(surveyRepository);
    }

    @Test
    @DisplayName("handle(UpdateSurveyCommand) -> throws RuntimeException when id does not exist (AAA)")
    void handleUpdateMissing() {
        // Arrange
        var command = FeedbackCommandFixtures.updateSurveyCommand(SURVEY_ID);
        when(surveyRepository.existsById(SURVEY_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(SURVEY_ID)).contains("does not exist");
        verify(surveyRepository, times(1)).existsById(SURVEY_ID);
        verifyNoMoreInteractions(surveyRepository);
    }

    @Test
    @DisplayName("handle(UpdateSurveyCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleUpdateSaveFailure() {
        // Arrange
        var existing = new Survey(FeedbackCommandFixtures.validCreateSurveyCommand());
        ReflectionTestUtils.setId(existing, SURVEY_ID);
        var command = FeedbackCommandFixtures.updateSurveyCommand(SURVEY_ID);
        when(surveyRepository.existsById(SURVEY_ID)).thenReturn(true);
        when(surveyRepository.findById(SURVEY_ID)).thenReturn(Optional.of(existing));
        when(surveyRepository.save(existing)).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error updating Survey").contains("boom");
        verify(surveyRepository, times(1)).existsById(SURVEY_ID);
        verify(surveyRepository, times(1)).findById(SURVEY_ID);
        verify(surveyRepository, times(1)).save(existing);
        verifyNoMoreInteractions(surveyRepository);
    }

    @Test
    @DisplayName("handle(DeleteSurveyCommand) -> deletes when present (AAA)")
    void handleDeleteSuccess() {
        // Arrange
        var command = new DeleteSurveyCommand(SURVEY_ID);
        when(surveyRepository.existsById(SURVEY_ID)).thenReturn(true);

        // Act
        service.handle(command);

        // Assert
        verify(surveyRepository, times(1)).existsById(SURVEY_ID);
        verify(surveyRepository, times(1)).deleteById(SURVEY_ID);
        verifyNoMoreInteractions(surveyRepository);
    }

    @Test
    @DisplayName("handle(DeleteSurveyCommand) -> throws RuntimeException when id is absent (AAA)")
    void handleDeleteMissing() {
        // Arrange
        var command = new DeleteSurveyCommand(SURVEY_ID);
        when(surveyRepository.existsById(SURVEY_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(SURVEY_ID)).contains("does not exist");
        verify(surveyRepository, times(1)).existsById(SURVEY_ID);
        verify(surveyRepository, never()).deleteById(any(Long.class));
        verifyNoMoreInteractions(surveyRepository);
    }

    @Test
    @DisplayName("handle(DeleteSurveyCommand) -> wraps deleteById failure in RuntimeException (AAA)")
    void handleDeleteDeleteFailure() {
        // Arrange
        var command = new DeleteSurveyCommand(SURVEY_ID);
        when(surveyRepository.existsById(SURVEY_ID)).thenReturn(true);
        doThrow(new RuntimeException("fk")).when(surveyRepository).deleteById(SURVEY_ID);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error deleting Survey").contains("fk");
        verify(surveyRepository, times(1)).existsById(SURVEY_ID);
        verify(surveyRepository, times(1)).deleteById(SURVEY_ID);
        verifyNoMoreInteractions(surveyRepository);
    }
}
