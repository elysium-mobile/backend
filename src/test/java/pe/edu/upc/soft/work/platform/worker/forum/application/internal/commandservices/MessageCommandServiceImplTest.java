package pe.edu.upc.soft.work.platform.worker.forum.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import pe.edu.upc.soft.work.platform.payment.service.application.internal.outboundservices.acl.ExternalIamServiceFromPaymentService;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;
import pe.edu.upc.soft.work.platform.worker.forum.application.internal.outboundservices.acl.ExternalIamServiceFromWorkerForum;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Message;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Thread;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteMessageCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.events.MessagePostedEvent;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.UserAccountId;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.AssetRepository;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.MessageRepository;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.ThreadRepository;
import pe.edu.upc.soft.work.platform.worker.forum.test.fixtures.WorkerForumCommandFixtures;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageCommandServiceImplTest {

    private static final Long MESSAGE_ID = 61L;

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private ExternalIamServiceFromWorkerForum externalIamServiceFromWorkerForum;

    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private ThreadRepository threadRepository;
    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private MessageCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateMessageCommand) -> creates Message when user account exists (AAA)")
    void handleCreateSuccess() {
        // Arrange
        var command = WorkerForumCommandFixtures.validCreateMessageCommand();
        var thread = mock(Thread.class);

        when(externalIamServiceFromWorkerForum.existsUserAccountById(any())).thenReturn(true);
        when(threadRepository.existsById(command.threadId())).thenReturn(true);
        when(threadRepository.findById(command.threadId())).thenReturn(Optional.of(thread));

        when(messageRepository.save(any(Message.class))).thenAnswer(inv -> {
            Message m = inv.getArgument(0);
            ReflectionTestUtils.setId(m, MESSAGE_ID);
            return m;
        });

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(MESSAGE_ID);
        verify(eventPublisher).publishEvent(any(MessagePostedEvent.class));
        verify(externalIamServiceFromWorkerForum).existsUserAccountById(any());
        verify(messageRepository).save(any(Message.class));
        verify(threadRepository).save(any(Thread.class));
        verifyNoMoreInteractions(externalIamServiceFromWorkerForum, messageRepository, threadRepository, eventPublisher);}

    @Test
    @DisplayName("handle(CreateMessageCommand) -> throws NotFoundArgumentException when user account is missing (AAA)")
    void handleCreateMissingUser() {
        // Arrange
        var command = WorkerForumCommandFixtures.validCreateMessageCommand();
        when(externalIamServiceFromWorkerForum.existsUserAccountById(WorkerForumCommandFixtures.VALID_USER_ACCOUNT_ID))
                .thenReturn(false);

        // Act + Assert
        NotFoundArgumentException ex = assertThrows(NotFoundArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("User Account ID: " + WorkerForumCommandFixtures.VALID_USER_ACCOUNT_ID);
        verify(externalIamServiceFromWorkerForum, times(1))
                .existsUserAccountById(WorkerForumCommandFixtures.VALID_USER_ACCOUNT_ID);
        verifyNoMoreInteractions(externalIamServiceFromWorkerForum);
        verifyNoInteractions(messageRepository);
    }

    @Test
    @DisplayName("handle(CreateMessageCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleCreateSaveFailure() {
        // Arrange
        var command = WorkerForumCommandFixtures.validCreateMessageCommand();
        var thread = mock(Thread.class);

        when(externalIamServiceFromWorkerForum.existsUserAccountById(any())).thenReturn(true);
        when(threadRepository.existsById(command.threadId())).thenReturn(true);
        when(threadRepository.findById(command.threadId())).thenReturn(Optional.of(thread));
        when(messageRepository.save(any(Message.class))).thenThrow(new RuntimeException("db"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error creating Message").contains("db");
        verify(externalIamServiceFromWorkerForum).existsUserAccountById(any());
        verify(threadRepository).existsById(command.threadId());
        verify(threadRepository).findById(command.threadId());
        verify(eventPublisher).publishEvent(any(MessagePostedEvent.class));
        verify(messageRepository).save(any(Message.class));
        verifyNoMoreInteractions(externalIamServiceFromWorkerForum, messageRepository, threadRepository, eventPublisher, assetRepository);
    }

    @Test
    @DisplayName("handle(UpdateMessageCommand) -> returns Optional with updated Message when present (AAA)")
    void handleUpdateSuccess() {
        // Arrange
        var command = WorkerForumCommandFixtures.updateMessageCommand(MESSAGE_ID);
        var commandUserAccountId = command.userAccountId();

        var existing = new Message(WorkerForumCommandFixtures.validCreateMessageCommand());
        ReflectionTestUtils.setId(existing, MESSAGE_ID);
        ReflectionTestUtils.setField(existing, "userAccountId", commandUserAccountId);

        when(threadRepository.existsById(command.threadId())).thenReturn(true);
        when(messageRepository.existsById(MESSAGE_ID)).thenReturn(true);
        when(messageRepository.findById(MESSAGE_ID)).thenReturn(Optional.of(existing));
        when(messageRepository.save(any(Message.class))).thenReturn(existing);

        // Act
        Optional<Message> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getContentMessage()).isEqualTo(WorkerForumCommandFixtures.VALID_MESSAGE_CONTENT);
        verify(threadRepository).existsById(command.threadId());
        verify(messageRepository).existsById(MESSAGE_ID);
        verify(messageRepository).findById(MESSAGE_ID);
        verify(messageRepository).save(any(Message.class));
        verifyNoMoreInteractions(messageRepository, threadRepository, eventPublisher, assetRepository);
        verifyNoInteractions(externalIamServiceFromWorkerForum);
    }

    @Test
    @DisplayName("handle(UpdateMessageCommand) -> throws RuntimeException when id does not exist (AAA)")
    void handleUpdateMissing() {
        // Arrange
        var command = WorkerForumCommandFixtures.updateMessageCommand(MESSAGE_ID);
        when(threadRepository.existsById(command.threadId())).thenReturn(true);
        when(messageRepository.existsById(MESSAGE_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));

        // Assert
        assertThat(ex.getMessage()).contains(String.valueOf(MESSAGE_ID)).contains("does not exist");
        verify(threadRepository).existsById(command.threadId());
        verify(messageRepository).existsById(MESSAGE_ID);
        verifyNoMoreInteractions(messageRepository, threadRepository, eventPublisher, assetRepository);
        verifyNoInteractions(externalIamServiceFromWorkerForum);
    }

    @Test
    @DisplayName("handle(UpdateMessageCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleUpdateSaveFailure() {
        // Arrange
        var existing = new Message(WorkerForumCommandFixtures.validCreateMessageCommand());
        ReflectionTestUtils.setId(existing, MESSAGE_ID);
        var userAccountId = WorkerForumCommandFixtures.VALID_USER_ACCOUNT_ID;
        ReflectionTestUtils.setField(existing, "userAccountId", new UserAccountId(userAccountId));
        var command = WorkerForumCommandFixtures.updateMessageCommand(MESSAGE_ID);
        when(threadRepository.existsById(command.threadId())).thenReturn(true);
        when(messageRepository.existsById(MESSAGE_ID)).thenReturn(true);
        when(messageRepository.findById(MESSAGE_ID)).thenReturn(Optional.of(existing));
        when(messageRepository.save(any(Message.class))).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error updating Message").contains("boom");
        verify(threadRepository).existsById(command.threadId());
        verify(messageRepository).existsById(MESSAGE_ID);
        verify(messageRepository).findById(MESSAGE_ID);
        verify(messageRepository).save(any(Message.class));
        verifyNoMoreInteractions(messageRepository, threadRepository, eventPublisher, assetRepository);
        verifyNoInteractions(externalIamServiceFromWorkerForum);
    }

    @Test
    @DisplayName("handle(DeleteMessageCommand) -> deletes when present (AAA)")
    void handleDeleteSuccess() {
        // Arrange
        var command = new DeleteMessageCommand(MESSAGE_ID);
        var message = mock(Message.class);
        var thread = mock(Thread.class);
        when(message.getThreadId()).thenReturn(11L);
        when(messageRepository.findById(MESSAGE_ID)).thenReturn(Optional.of(message));
        when(threadRepository.findById(11L)).thenReturn(Optional.of(thread));

        // Act
        service.handle(command);

        // Assert
        verify(messageRepository).findById(MESSAGE_ID);
        verify(threadRepository).findById(11L);
        verify(thread).removeMessage(MESSAGE_ID);
        verify(thread).decrementMessageCount();
        verify(threadRepository).save(thread);
        verifyNoMoreInteractions(messageRepository, threadRepository, eventPublisher, assetRepository);
        verifyNoInteractions(externalIamServiceFromWorkerForum);
    }

    @Test
    @DisplayName("handle(DeleteMessageCommand) -> throws RuntimeException when id is absent (AAA)")
    void handleDeleteMissing() {
        // Arrange
        var command = new DeleteMessageCommand(MESSAGE_ID);
        when(messageRepository.findById(MESSAGE_ID)).thenReturn(Optional.empty());

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(MESSAGE_ID)).contains("does not exist");
        verify(messageRepository).findById(MESSAGE_ID);
        verifyNoMoreInteractions(messageRepository, threadRepository, assetRepository, eventPublisher);
        verifyNoInteractions(externalIamServiceFromWorkerForum);
    }

    @Test
    @DisplayName("handle(DeleteMessageCommand) -> wraps deleteById failure in RuntimeException (AAA)")
    void handleDeleteDeleteFailure() {
        // Arrange
        var command = new DeleteMessageCommand(MESSAGE_ID);
        var message = mock(Message.class);
        var thread = mock(Thread.class);
        when(message.getThreadId()).thenReturn(11L);
        when(messageRepository.findById(MESSAGE_ID)).thenReturn(Optional.of(message));
        when(threadRepository.findById(11L)).thenReturn(Optional.of(thread));
        doThrow(new RuntimeException("fk")).when(threadRepository).save(any(Thread.class));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error deleting Message").contains("fk");
        verify(messageRepository).findById(MESSAGE_ID);
        verify(threadRepository).findById(11L);
        verify(thread).removeMessage(MESSAGE_ID);
        verify(thread).decrementMessageCount();
        verify(threadRepository).save(any(Thread.class));
        verifyNoMoreInteractions(messageRepository, threadRepository, eventPublisher, assetRepository);
        verifyNoInteractions(externalIamServiceFromWorkerForum);
    }
}
