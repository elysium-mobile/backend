package pe.edu.upc.soft.work.platform.worker.forum.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.payment.service.application.internal.outboundservices.acl.ExternalIamServiceFromPaymentService;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Message;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteMessageCommand;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.MessageRepository;
import pe.edu.upc.soft.work.platform.worker.forum.test.fixtures.WorkerForumCommandFixtures;

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
class MessageCommandServiceImplTest {

    private static final Long MESSAGE_ID = 61L;

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private ExternalIamServiceFromPaymentService externalIamServiceFromPaymentService;

    @InjectMocks
    private MessageCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateMessageCommand) -> creates Message when user account exists (AAA)")
    void handleCreateSuccess() {
        // Arrange
        var command = WorkerForumCommandFixtures.validCreateMessageCommand();
        when(externalIamServiceFromPaymentService.existsUserAccountById(WorkerForumCommandFixtures.VALID_USER_ACCOUNT_ID))
                .thenReturn(true);
        when(messageRepository.save(any(Message.class))).thenAnswer(inv -> {
            Message m = inv.getArgument(0);
            ReflectionTestUtils.setId(m, MESSAGE_ID);
            return m;
        });

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(MESSAGE_ID);
        verify(externalIamServiceFromPaymentService, times(1))
                .existsUserAccountById(WorkerForumCommandFixtures.VALID_USER_ACCOUNT_ID);
        verify(messageRepository, times(1)).save(any(Message.class));
        verifyNoMoreInteractions(externalIamServiceFromPaymentService, messageRepository);
    }

    @Test
    @DisplayName("handle(CreateMessageCommand) -> throws NotFoundArgumentException when user account is missing (AAA)")
    void handleCreateMissingUser() {
        // Arrange
        var command = WorkerForumCommandFixtures.validCreateMessageCommand();
        when(externalIamServiceFromPaymentService.existsUserAccountById(WorkerForumCommandFixtures.VALID_USER_ACCOUNT_ID))
                .thenReturn(false);

        // Act + Assert
        NotFoundArgumentException ex = assertThrows(NotFoundArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("User Account ID: " + WorkerForumCommandFixtures.VALID_USER_ACCOUNT_ID);
        verify(externalIamServiceFromPaymentService, times(1))
                .existsUserAccountById(WorkerForumCommandFixtures.VALID_USER_ACCOUNT_ID);
        verifyNoMoreInteractions(externalIamServiceFromPaymentService);
        verifyNoInteractions(messageRepository);
    }

    @Test
    @DisplayName("handle(CreateMessageCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleCreateSaveFailure() {
        // Arrange
        var command = WorkerForumCommandFixtures.validCreateMessageCommand();
        when(externalIamServiceFromPaymentService.existsUserAccountById(WorkerForumCommandFixtures.VALID_USER_ACCOUNT_ID))
                .thenReturn(true);
        when(messageRepository.save(any(Message.class))).thenThrow(new RuntimeException("db"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error creating Message").contains("db");
        verify(externalIamServiceFromPaymentService, times(1))
                .existsUserAccountById(WorkerForumCommandFixtures.VALID_USER_ACCOUNT_ID);
        verify(messageRepository, times(1)).save(any(Message.class));
        verifyNoMoreInteractions(externalIamServiceFromPaymentService, messageRepository);
    }

    @Test
    @DisplayName("handle(UpdateMessageCommand) -> returns Optional with updated Message when present (AAA)")
    void handleUpdateSuccess() {
        // Arrange
        var existing = new Message(WorkerForumCommandFixtures.validCreateMessageCommand());
        ReflectionTestUtils.setId(existing, MESSAGE_ID);
        var command = WorkerForumCommandFixtures.updateMessageCommand(MESSAGE_ID);
        when(messageRepository.existsById(MESSAGE_ID)).thenReturn(true);
        when(messageRepository.findById(MESSAGE_ID)).thenReturn(Optional.of(existing));
        when(messageRepository.save(existing)).thenReturn(existing);

        // Act
        Optional<Message> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getContentMessage()).isEqualTo(WorkerForumCommandFixtures.VALID_MESSAGE_CONTENT);
        verify(messageRepository, times(1)).existsById(MESSAGE_ID);
        verify(messageRepository, times(1)).findById(MESSAGE_ID);
        verify(messageRepository, times(1)).save(existing);
        verifyNoMoreInteractions(messageRepository);
        verifyNoInteractions(externalIamServiceFromPaymentService);
    }

    @Test
    @DisplayName("handle(UpdateMessageCommand) -> throws RuntimeException when id does not exist (AAA)")
    void handleUpdateMissing() {
        // Arrange
        var command = WorkerForumCommandFixtures.updateMessageCommand(MESSAGE_ID);
        when(messageRepository.existsById(MESSAGE_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(MESSAGE_ID)).contains("does not exist");
        verify(messageRepository, times(1)).existsById(MESSAGE_ID);
        verifyNoMoreInteractions(messageRepository);
        verifyNoInteractions(externalIamServiceFromPaymentService);
    }

    @Test
    @DisplayName("handle(UpdateMessageCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleUpdateSaveFailure() {
        // Arrange
        var existing = new Message(WorkerForumCommandFixtures.validCreateMessageCommand());
        ReflectionTestUtils.setId(existing, MESSAGE_ID);
        var command = WorkerForumCommandFixtures.updateMessageCommand(MESSAGE_ID);
        when(messageRepository.existsById(MESSAGE_ID)).thenReturn(true);
        when(messageRepository.findById(MESSAGE_ID)).thenReturn(Optional.of(existing));
        when(messageRepository.save(existing)).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error updating Message").contains("boom");
        verify(messageRepository, times(1)).existsById(MESSAGE_ID);
        verify(messageRepository, times(1)).findById(MESSAGE_ID);
        verify(messageRepository, times(1)).save(existing);
        verifyNoMoreInteractions(messageRepository);
        verifyNoInteractions(externalIamServiceFromPaymentService);
    }

    @Test
    @DisplayName("handle(DeleteMessageCommand) -> deletes when present (AAA)")
    void handleDeleteSuccess() {
        // Arrange
        var command = new DeleteMessageCommand(MESSAGE_ID);
        when(messageRepository.existsById(MESSAGE_ID)).thenReturn(true);

        // Act
        service.handle(command);

        // Assert
        verify(messageRepository, times(1)).existsById(MESSAGE_ID);
        verify(messageRepository, times(1)).deleteById(MESSAGE_ID);
        verifyNoMoreInteractions(messageRepository);
        verifyNoInteractions(externalIamServiceFromPaymentService);
    }

    @Test
    @DisplayName("handle(DeleteMessageCommand) -> throws RuntimeException when id is absent (AAA)")
    void handleDeleteMissing() {
        // Arrange
        var command = new DeleteMessageCommand(MESSAGE_ID);
        when(messageRepository.existsById(MESSAGE_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(MESSAGE_ID)).contains("does not exist");
        verify(messageRepository, times(1)).existsById(MESSAGE_ID);
        verify(messageRepository, never()).deleteById(any(Long.class));
        verifyNoMoreInteractions(messageRepository);
        verifyNoInteractions(externalIamServiceFromPaymentService);
    }

    @Test
    @DisplayName("handle(DeleteMessageCommand) -> wraps deleteById failure in RuntimeException (AAA)")
    void handleDeleteDeleteFailure() {
        // Arrange
        var command = new DeleteMessageCommand(MESSAGE_ID);
        when(messageRepository.existsById(MESSAGE_ID)).thenReturn(true);
        doThrow(new RuntimeException("fk")).when(messageRepository).deleteById(MESSAGE_ID);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error deleting Message").contains("fk");
        verify(messageRepository, times(1)).existsById(MESSAGE_ID);
        verify(messageRepository, times(1)).deleteById(MESSAGE_ID);
        verifyNoMoreInteractions(messageRepository);
        verifyNoInteractions(externalIamServiceFromPaymentService);
    }
}
