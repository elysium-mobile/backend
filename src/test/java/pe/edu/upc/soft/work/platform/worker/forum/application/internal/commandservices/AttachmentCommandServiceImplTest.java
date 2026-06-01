package pe.edu.upc.soft.work.platform.worker.forum.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteAttachmentCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Attachment;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.AttachmentRepository;
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
class AttachmentCommandServiceImplTest {

    private static final Long ATTACHMENT_ID = 31L;

    @Mock
    private AttachmentRepository attachmentRepository;
    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private AttachmentCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateAttachmentCommand) -> creates Attachment when guard passes (AAA)")
    void handleCreateSuccess() {
        // Arrange
        var command = WorkerForumCommandFixtures.validCreateAttachmentCommand();
        // NOTE (source quirk): the service guards via attachmentRepository.existsById(command.messageId())
        when(attachmentRepository.existsById(WorkerForumCommandFixtures.VALID_MESSAGE_ID)).thenReturn(true);
        when(attachmentRepository.save(any(Attachment.class))).thenAnswer(inv -> {
            Attachment a = inv.getArgument(0);
            ReflectionTestUtils.setId(a, ATTACHMENT_ID);
            return a;
        });

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(ATTACHMENT_ID);
        verify(attachmentRepository, times(1)).existsById(WorkerForumCommandFixtures.VALID_MESSAGE_ID);
        verify(attachmentRepository, times(1)).save(any(Attachment.class));
        verifyNoMoreInteractions(attachmentRepository);
        verifyNoInteractions(messageRepository);
    }

    @Test
    @DisplayName("handle(CreateAttachmentCommand) -> throws NotFoundArgumentException when guard returns false (AAA)")
    void handleCreateGuardFails() {
        // Arrange
        var command = WorkerForumCommandFixtures.validCreateAttachmentCommand();
        when(attachmentRepository.existsById(WorkerForumCommandFixtures.VALID_MESSAGE_ID)).thenReturn(false);

        // Act + Assert
        NotFoundArgumentException ex = assertThrows(NotFoundArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Message ID: " + WorkerForumCommandFixtures.VALID_MESSAGE_ID);
        verify(attachmentRepository, times(1)).existsById(WorkerForumCommandFixtures.VALID_MESSAGE_ID);
        verifyNoMoreInteractions(attachmentRepository);
        verifyNoInteractions(messageRepository);
    }

    @Test
    @DisplayName("handle(CreateAttachmentCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleCreateSaveFailure() {
        // Arrange
        var command = WorkerForumCommandFixtures.validCreateAttachmentCommand();
        when(attachmentRepository.existsById(WorkerForumCommandFixtures.VALID_MESSAGE_ID)).thenReturn(true);
        when(attachmentRepository.save(any(Attachment.class))).thenThrow(new RuntimeException("db"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error creating Attachment").contains("db");
        verify(attachmentRepository, times(1)).existsById(WorkerForumCommandFixtures.VALID_MESSAGE_ID);
        verify(attachmentRepository, times(1)).save(any(Attachment.class));
        verifyNoMoreInteractions(attachmentRepository);
        verifyNoInteractions(messageRepository);
    }

    @Test
    @DisplayName("handle(UpdateAttachmentCommand) -> returns Optional with updated Attachment when present (AAA)")
    void handleUpdateSuccess() {
        // Arrange
        var existing = new Attachment(WorkerForumCommandFixtures.validCreateAttachmentCommand());
        ReflectionTestUtils.setId(existing, ATTACHMENT_ID);
        var command = WorkerForumCommandFixtures.updateAttachmentCommand(ATTACHMENT_ID);
        when(attachmentRepository.existsById(ATTACHMENT_ID)).thenReturn(true);
        when(attachmentRepository.findById(ATTACHMENT_ID)).thenReturn(Optional.of(existing));
        when(attachmentRepository.save(existing)).thenReturn(existing);

        // Act
        Optional<Attachment> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(WorkerForumCommandFixtures.VALID_ATTACHMENT_NAME);
        verify(attachmentRepository, times(1)).existsById(ATTACHMENT_ID);
        verify(attachmentRepository, times(1)).findById(ATTACHMENT_ID);
        verify(attachmentRepository, times(1)).save(existing);
        verifyNoMoreInteractions(attachmentRepository);
        verifyNoInteractions(messageRepository);
    }

    @Test
    @DisplayName("handle(UpdateAttachmentCommand) -> throws RuntimeException when id does not exist (AAA)")
    void handleUpdateMissing() {
        // Arrange
        var command = WorkerForumCommandFixtures.updateAttachmentCommand(ATTACHMENT_ID);
        when(attachmentRepository.existsById(ATTACHMENT_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(ATTACHMENT_ID)).contains("does not exist");
        verify(attachmentRepository, times(1)).existsById(ATTACHMENT_ID);
        verifyNoMoreInteractions(attachmentRepository);
        verifyNoInteractions(messageRepository);
    }

    @Test
    @DisplayName("handle(UpdateAttachmentCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleUpdateSaveFailure() {
        // Arrange
        var existing = new Attachment(WorkerForumCommandFixtures.validCreateAttachmentCommand());
        ReflectionTestUtils.setId(existing, ATTACHMENT_ID);
        var command = WorkerForumCommandFixtures.updateAttachmentCommand(ATTACHMENT_ID);
        when(attachmentRepository.existsById(ATTACHMENT_ID)).thenReturn(true);
        when(attachmentRepository.findById(ATTACHMENT_ID)).thenReturn(Optional.of(existing));
        when(attachmentRepository.save(existing)).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error updating Attachment").contains("boom");
        verify(attachmentRepository, times(1)).existsById(ATTACHMENT_ID);
        verify(attachmentRepository, times(1)).findById(ATTACHMENT_ID);
        verify(attachmentRepository, times(1)).save(existing);
        verifyNoMoreInteractions(attachmentRepository);
        verifyNoInteractions(messageRepository);
    }

    @Test
    @DisplayName("handle(DeleteAttachmentCommand) -> deletes when present (AAA)")
    void handleDeleteSuccess() {
        // Arrange
        var command = new DeleteAttachmentCommand(ATTACHMENT_ID);
        when(attachmentRepository.existsById(ATTACHMENT_ID)).thenReturn(true);

        // Act
        service.handle(command);

        // Assert
        verify(attachmentRepository, times(1)).existsById(ATTACHMENT_ID);
        verify(attachmentRepository, times(1)).deleteById(ATTACHMENT_ID);
        verifyNoMoreInteractions(attachmentRepository);
        verifyNoInteractions(messageRepository);
    }

    @Test
    @DisplayName("handle(DeleteAttachmentCommand) -> throws RuntimeException when id is absent (AAA)")
    void handleDeleteMissing() {
        // Arrange
        var command = new DeleteAttachmentCommand(ATTACHMENT_ID);
        when(attachmentRepository.existsById(ATTACHMENT_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(ATTACHMENT_ID)).contains("does not exist");
        verify(attachmentRepository, times(1)).existsById(ATTACHMENT_ID);
        verify(attachmentRepository, never()).deleteById(any(Long.class));
        verifyNoMoreInteractions(attachmentRepository);
        verifyNoInteractions(messageRepository);
    }

    @Test
    @DisplayName("handle(DeleteAttachmentCommand) -> wraps deleteById failure in RuntimeException (AAA)")
    void handleDeleteDeleteFailure() {
        // Arrange
        var command = new DeleteAttachmentCommand(ATTACHMENT_ID);
        when(attachmentRepository.existsById(ATTACHMENT_ID)).thenReturn(true);
        doThrow(new RuntimeException("fk")).when(attachmentRepository).deleteById(ATTACHMENT_ID);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error deleting Attachment").contains("fk");
        verify(attachmentRepository, times(1)).existsById(ATTACHMENT_ID);
        verify(attachmentRepository, times(1)).deleteById(ATTACHMENT_ID);
        verifyNoMoreInteractions(attachmentRepository);
        verifyNoInteractions(messageRepository);
    }
}
