package pe.edu.upc.soft.work.platform.feedback.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.dashboard.application.internal.outboundservices.acl.ExternalIamServiceFromDashboard;
import pe.edu.upc.soft.work.platform.feedback.domain.model.commands.DeleteAnswerCommand;
import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.Answer;
import pe.edu.upc.soft.work.platform.feedback.infrastructure.persistence.jpa.repositories.AnswerRepository;
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
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnswerCommandServiceImplTest {

    private static final Long ANSWER_ID = 21L;

    @Mock
    private AnswerRepository answerRepository;
    @Mock
    private ExternalIamServiceFromDashboard externalIamServiceFromDashboard;

    @InjectMocks
    private AnswerCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateAnswerCommand) -> creates Answer and returns generated id (AAA)")
    void handleCreateSuccess() {
        // Arrange
        var command = FeedbackCommandFixtures.validCreateAnswerCommand();
        when(answerRepository.save(any(Answer.class))).thenAnswer(inv -> {
            Answer a = inv.getArgument(0);
            ReflectionTestUtils.setId(a, ANSWER_ID);
            return a;
        });

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(ANSWER_ID);
        verify(answerRepository, times(1)).save(any(Answer.class));
        verifyNoMoreInteractions(answerRepository);
        verifyNoInteractions(externalIamServiceFromDashboard);
    }

    @Test
    @DisplayName("handle(CreateAnswerCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleCreateSaveFailure() {
        // Arrange
        var command = FeedbackCommandFixtures.validCreateAnswerCommand();
        when(answerRepository.save(any(Answer.class))).thenThrow(new RuntimeException("db"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error creating Answer").contains("db");
        verify(answerRepository, times(1)).save(any(Answer.class));
        verifyNoMoreInteractions(answerRepository);
        verifyNoInteractions(externalIamServiceFromDashboard);
    }

    @Test
    @DisplayName("handle(UpdateAnswerCommand) -> returns Optional with updated Answer when present (AAA)")
    void handleUpdateSuccess() {
        // Arrange
        var existing = new Answer(FeedbackCommandFixtures.validCreateAnswerCommand());
        ReflectionTestUtils.setId(existing, ANSWER_ID);
        var command = FeedbackCommandFixtures.updateAnswerCommand(ANSWER_ID);
        when(answerRepository.existsById(ANSWER_ID)).thenReturn(true);
        when(answerRepository.findById(ANSWER_ID)).thenReturn(Optional.of(existing));
        when(answerRepository.save(existing)).thenReturn(existing);

        // Act
        Optional<Answer> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getValue()).isEqualTo(FeedbackCommandFixtures.VALID_ANSWER_VALUE);
        assertThat(result.get().getScoreAnswer()).isEqualTo(FeedbackCommandFixtures.VALID_ANSWER_SCORE);
        verify(answerRepository, times(1)).existsById(ANSWER_ID);
        verify(answerRepository, times(1)).findById(ANSWER_ID);
        verify(answerRepository, times(1)).save(existing);
        verifyNoMoreInteractions(answerRepository);
        verifyNoInteractions(externalIamServiceFromDashboard);
    }

    @Test
    @DisplayName("handle(UpdateAnswerCommand) -> throws RuntimeException when id does not exist (AAA)")
    void handleUpdateMissing() {
        // Arrange
        var command = FeedbackCommandFixtures.updateAnswerCommand(ANSWER_ID);
        when(answerRepository.existsById(ANSWER_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(ANSWER_ID)).contains("does not exist");
        verify(answerRepository, times(1)).existsById(ANSWER_ID);
        verifyNoMoreInteractions(answerRepository);
        verifyNoInteractions(externalIamServiceFromDashboard);
    }

    @Test
    @DisplayName("handle(UpdateAnswerCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleUpdateSaveFailure() {
        // Arrange
        var existing = new Answer(FeedbackCommandFixtures.validCreateAnswerCommand());
        ReflectionTestUtils.setId(existing, ANSWER_ID);
        var command = FeedbackCommandFixtures.updateAnswerCommand(ANSWER_ID);
        when(answerRepository.existsById(ANSWER_ID)).thenReturn(true);
        when(answerRepository.findById(ANSWER_ID)).thenReturn(Optional.of(existing));
        when(answerRepository.save(existing)).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error updating Answer").contains("boom");
        verify(answerRepository, times(1)).existsById(ANSWER_ID);
        verify(answerRepository, times(1)).findById(ANSWER_ID);
        verify(answerRepository, times(1)).save(existing);
        verifyNoMoreInteractions(answerRepository);
        verifyNoInteractions(externalIamServiceFromDashboard);
    }

    @Test
    @DisplayName("handle(DeleteAnswerCommand) -> deletes when present (AAA)")
    void handleDeleteSuccess() {
        // Arrange
        var command = new DeleteAnswerCommand(ANSWER_ID);
        when(answerRepository.existsById(ANSWER_ID)).thenReturn(true);

        // Act
        service.handle(command);

        // Assert
        verify(answerRepository, times(1)).existsById(ANSWER_ID);
        verify(answerRepository, times(1)).deleteById(ANSWER_ID);
        verifyNoMoreInteractions(answerRepository);
        verifyNoInteractions(externalIamServiceFromDashboard);
    }

    @Test
    @DisplayName("handle(DeleteAnswerCommand) -> throws RuntimeException when id is absent (AAA)")
    void handleDeleteMissing() {
        // Arrange
        var command = new DeleteAnswerCommand(ANSWER_ID);
        when(answerRepository.existsById(ANSWER_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(ANSWER_ID)).contains("does not exist");
        verify(answerRepository, times(1)).existsById(ANSWER_ID);
        verify(answerRepository, never()).deleteById(any(Long.class));
        verifyNoMoreInteractions(answerRepository);
        verifyNoInteractions(externalIamServiceFromDashboard);
    }

    @Test
    @DisplayName("handle(DeleteAnswerCommand) -> wraps deleteById failure in RuntimeException (AAA)")
    void handleDeleteDeleteFailure() {
        // Arrange
        var command = new DeleteAnswerCommand(ANSWER_ID);
        when(answerRepository.existsById(ANSWER_ID)).thenReturn(true);
        doThrow(new RuntimeException("fk")).when(answerRepository).deleteById(ANSWER_ID);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error deleting Answer").contains("fk");
        verify(answerRepository, times(1)).existsById(ANSWER_ID);
        verify(answerRepository, times(1)).deleteById(ANSWER_ID);
        verifyNoMoreInteractions(answerRepository);
        verifyNoInteractions(externalIamServiceFromDashboard);
    }
}
