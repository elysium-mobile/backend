package pe.edu.upc.soft.work.platform.feedback.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.DeleteQuestionSurveyCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.QuestionSurvey;
import pe.edu.upc.soft.work.platform.feedback.infrastructure.persistence.jpa.repositories.QuestionSurveyRepository;
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
class QuestionSurveyCommandServiceImplTest {

    private static final Long QS_ID = 14L;

    @Mock
    private QuestionSurveyRepository questionsurveyRepository;

    @InjectMocks
    private QuestionSurveyCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateQuestionSurveyCommand) -> creates QuestionSurvey and returns generated id (AAA)")
    void handleCreateSuccess() {
        // Arrange
        var command = FeedbackCommandFixtures.validCreateQuestionSurveyCommand();
        when(questionsurveyRepository.save(any(QuestionSurvey.class))).thenAnswer(inv -> {
            QuestionSurvey q = inv.getArgument(0);
            ReflectionTestUtils.setId(q, QS_ID);
            return q;
        });

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(QS_ID);
        verify(questionsurveyRepository, times(1)).save(any(QuestionSurvey.class));
        verifyNoMoreInteractions(questionsurveyRepository);
    }

    @Test
    @DisplayName("handle(CreateQuestionSurveyCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleCreateSaveFailure() {
        // Arrange
        var command = FeedbackCommandFixtures.validCreateQuestionSurveyCommand();
        when(questionsurveyRepository.save(any(QuestionSurvey.class))).thenThrow(new RuntimeException("db"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error creating QuestionSurvey").contains("db");
        verify(questionsurveyRepository, times(1)).save(any(QuestionSurvey.class));
        verifyNoMoreInteractions(questionsurveyRepository);
    }

    @Test
    @DisplayName("handle(UpdateQuestionSurveyCommand) -> returns Optional with updated QuestionSurvey when present (AAA)")
    void handleUpdateSuccess() {
        // Arrange
        var existing = new QuestionSurvey(FeedbackCommandFixtures.validCreateQuestionSurveyCommand());
        ReflectionTestUtils.setId(existing, QS_ID);
        var command = FeedbackCommandFixtures.updateQuestionSurveyCommand(QS_ID);
        when(questionsurveyRepository.existsById(QS_ID)).thenReturn(true);
        when(questionsurveyRepository.findById(QS_ID)).thenReturn(Optional.of(existing));
        when(questionsurveyRepository.save(existing)).thenReturn(existing);

        // Act
        Optional<QuestionSurvey> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getTextQuestion()).isEqualTo(FeedbackCommandFixtures.VALID_QUESTION_TEXT);
        assertThat(result.get().getQuestionType()).isEqualTo(FeedbackCommandFixtures.VALID_QUESTION_TYPE);
        verify(questionsurveyRepository, times(1)).existsById(QS_ID);
        verify(questionsurveyRepository, times(1)).findById(QS_ID);
        verify(questionsurveyRepository, times(1)).save(existing);
        verifyNoMoreInteractions(questionsurveyRepository);
    }

    @Test
    @DisplayName("handle(UpdateQuestionSurveyCommand) -> throws RuntimeException when id does not exist (AAA)")
    void handleUpdateMissing() {
        // Arrange
        var command = FeedbackCommandFixtures.updateQuestionSurveyCommand(QS_ID);
        when(questionsurveyRepository.existsById(QS_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(QS_ID)).contains("does not exist");
        verify(questionsurveyRepository, times(1)).existsById(QS_ID);
        verifyNoMoreInteractions(questionsurveyRepository);
    }

    @Test
    @DisplayName("handle(UpdateQuestionSurveyCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleUpdateSaveFailure() {
        // Arrange
        var existing = new QuestionSurvey(FeedbackCommandFixtures.validCreateQuestionSurveyCommand());
        ReflectionTestUtils.setId(existing, QS_ID);
        var command = FeedbackCommandFixtures.updateQuestionSurveyCommand(QS_ID);
        when(questionsurveyRepository.existsById(QS_ID)).thenReturn(true);
        when(questionsurveyRepository.findById(QS_ID)).thenReturn(Optional.of(existing));
        when(questionsurveyRepository.save(existing)).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error updating QuestionSurvey").contains("boom");
        verify(questionsurveyRepository, times(1)).existsById(QS_ID);
        verify(questionsurveyRepository, times(1)).findById(QS_ID);
        verify(questionsurveyRepository, times(1)).save(existing);
        verifyNoMoreInteractions(questionsurveyRepository);
    }

    @Test
    @DisplayName("handle(DeleteQuestionSurveyCommand) -> deletes when present (AAA)")
    void handleDeleteSuccess() {
        // Arrange
        var command = new DeleteQuestionSurveyCommand(QS_ID);
        when(questionsurveyRepository.existsById(QS_ID)).thenReturn(true);

        // Act
        service.handle(command);

        // Assert
        verify(questionsurveyRepository, times(1)).existsById(QS_ID);
        verify(questionsurveyRepository, times(1)).deleteById(QS_ID);
        verifyNoMoreInteractions(questionsurveyRepository);
    }

    @Test
    @DisplayName("handle(DeleteQuestionSurveyCommand) -> throws RuntimeException when id is absent (AAA)")
    void handleDeleteMissing() {
        // Arrange
        var command = new DeleteQuestionSurveyCommand(QS_ID);
        when(questionsurveyRepository.existsById(QS_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(QS_ID)).contains("does not exist");
        verify(questionsurveyRepository, times(1)).existsById(QS_ID);
        verify(questionsurveyRepository, never()).deleteById(any(Long.class));
        verifyNoMoreInteractions(questionsurveyRepository);
    }

    @Test
    @DisplayName("handle(DeleteQuestionSurveyCommand) -> wraps deleteById failure in RuntimeException (AAA)")
    void handleDeleteDeleteFailure() {
        // Arrange
        var command = new DeleteQuestionSurveyCommand(QS_ID);
        when(questionsurveyRepository.existsById(QS_ID)).thenReturn(true);
        doThrow(new RuntimeException("fk")).when(questionsurveyRepository).deleteById(QS_ID);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error deleting QuestionSurvey").contains("fk");
        verify(questionsurveyRepository, times(1)).existsById(QS_ID);
        verify(questionsurveyRepository, times(1)).deleteById(QS_ID);
        verifyNoMoreInteractions(questionsurveyRepository);
    }
}
