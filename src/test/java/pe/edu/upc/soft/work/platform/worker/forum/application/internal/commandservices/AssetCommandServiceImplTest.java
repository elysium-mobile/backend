package pe.edu.upc.soft.work.platform.worker.forum.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Message;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteAssetCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Asset;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.AssetFactory;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.AssetRepository;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.MessageRepository;
import pe.edu.upc.soft.work.platform.worker.forum.test.fixtures.WorkerForumCommandFixtures;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetCommandServiceImplTest {

    private static final Long ATTACHMENT_ID = 31L;

    @Mock
    private AssetRepository assetRepository;
    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private AssetCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateAttachmentCommand) -> creates Attachment when guard passes (AAA)")
    void handleCreateSuccess() {
        // Arrange
        var command = WorkerForumCommandFixtures.validCreateAssetCommand();
        when(messageRepository.existsById(WorkerForumCommandFixtures.VALID_MESSAGE_ID)).thenReturn(true);
        when(assetRepository.save(any(Asset.class))).thenAnswer(inv -> {
            Asset a = inv.getArgument(0);
            ReflectionTestUtils.setId(a, ATTACHMENT_ID);
            return a;
        });

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(ATTACHMENT_ID);
        verify(messageRepository, times(1)).existsById(WorkerForumCommandFixtures.VALID_MESSAGE_ID);
        verify(assetRepository, times(1)).save(any(Asset.class));
        verifyNoMoreInteractions(assetRepository, messageRepository);
    }

    @Test
    @DisplayName("handle(CreateAttachmentCommand) -> throws NotFoundArgumentException when guard returns false (AAA)")
    void handleCreateGuardFails() {
        // Arrange
        var command = WorkerForumCommandFixtures.validCreateAssetCommand();

        // Act + Assert
        NotFoundArgumentException ex = assertThrows(NotFoundArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Message ID: " + WorkerForumCommandFixtures.VALID_MESSAGE_ID);
        verify(messageRepository, times(1)).existsById(WorkerForumCommandFixtures.VALID_MESSAGE_ID);
        verifyNoMoreInteractions(assetRepository, messageRepository);
    }

    @Test
    @DisplayName("handle(CreateAttachmentCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleCreateSaveFailure() {
        // Arrange
        var command = WorkerForumCommandFixtures.validCreateAssetCommand();
        when(messageRepository.existsById(WorkerForumCommandFixtures.VALID_MESSAGE_ID)).thenReturn(true);
        when(assetRepository.save(any(Asset.class))).thenThrow(new RuntimeException("db"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error creating Attachment").contains("db");
        verify(messageRepository, times(1)).existsById(WorkerForumCommandFixtures.VALID_MESSAGE_ID);
        verify(assetRepository, times(1)).save(any(Asset.class));
        verifyNoMoreInteractions(assetRepository, messageRepository);
    }

    @Test
    @DisplayName("handle(UpdateAttachmentCommand) -> returns Optional with updated Attachment when present (AAA)")
    void handleUpdateSuccess() {
        // Arrange
        var command = WorkerForumCommandFixtures.validCreateAssetCommand();
        var existing = AssetFactory.create(
            command.messageId(),
            command.name(),
            command.url(),
            command.fileSize(),
            command.fileType()
        );
        ReflectionTestUtils.setId(existing, ATTACHMENT_ID);
        var updateCommand = WorkerForumCommandFixtures.updateAssetCommand(ATTACHMENT_ID);
        when(assetRepository.findById(ATTACHMENT_ID)).thenReturn(Optional.of(existing));
        when(assetRepository.save(existing)).thenReturn(existing);

        // Act
        Optional<Asset> result = service.handle(updateCommand);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(WorkerForumCommandFixtures.VALID_ATTACHMENT_NAME);
        verify(assetRepository, times(1)).findById(ATTACHMENT_ID);
        verify(assetRepository, times(1)).save(existing);
        verifyNoMoreInteractions(assetRepository);
        verifyNoInteractions(messageRepository);
    }

    @Test
    @DisplayName("handle(UpdateAttachmentCommand) -> throws RuntimeException when id does not exist (AAA)")
    void handleUpdateMissing() {
        // Arrange
        var command = WorkerForumCommandFixtures.updateAssetCommand(ATTACHMENT_ID);
        when(assetRepository.findById(ATTACHMENT_ID)).thenReturn(Optional.empty());

        // Act + Assert
        NotFoundArgumentException ex = assertThrows(NotFoundArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(ATTACHMENT_ID)).contains("not found");
        verify(assetRepository, times(1)).findById(ATTACHMENT_ID);
        verifyNoMoreInteractions(assetRepository);
        verifyNoInteractions(messageRepository);
    }

    @Test
    @DisplayName("handle(UpdateAttachmentCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleUpdateSaveFailure() {
        // Arrange
        var existing = mock(Asset.class);
        ReflectionTestUtils.setId(existing, ATTACHMENT_ID);
        var updateCommand = WorkerForumCommandFixtures.updateAssetCommand(ATTACHMENT_ID);
        when(assetRepository.findById(ATTACHMENT_ID)).thenReturn(Optional.of(existing));
        when(assetRepository.save(any(Asset.class))).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(updateCommand));
        assertThat(ex.getMessage()).contains("Error updating Asset").contains("boom");
        verify(assetRepository).findById(ATTACHMENT_ID);
        verify(assetRepository).save(any(Asset.class));
        verifyNoMoreInteractions(assetRepository);
        verifyNoInteractions(messageRepository);
    }

    @Test
    @DisplayName("handle(DeleteAttachmentCommand) -> deletes when present (AAA)")
    void handleDeleteSuccess() {
        // Arrange
        var command = new DeleteAssetCommand(ATTACHMENT_ID);
        Asset mockAsset = mock(Asset.class);
        when(mockAsset.getMessageId()).thenReturn(10L);
        Message mockMessage = mock(Message.class);
        when(assetRepository.findById(ATTACHMENT_ID)).thenReturn(Optional.of(mockAsset));
        when(messageRepository.findById(10L)).thenReturn(Optional.of(mockMessage));

        // Act
        service.handle(command);

        // Assert
        verify(assetRepository, times(1)).findById(ATTACHMENT_ID);
        verify(messageRepository, times(1)).findById(10L);
        verify(mockMessage, times(1)).removeAttachment(ATTACHMENT_ID);
        verify(messageRepository, times(1)).save(mockMessage);
        verifyNoMoreInteractions(assetRepository, messageRepository);
    }

    @Test
    @DisplayName("handle(DeleteAttachmentCommand) -> throws RuntimeException when id is absent (AAA)")
    void handleDeleteMissing() {
        // Arrange
        var command = new DeleteAssetCommand(ATTACHMENT_ID);
        when(assetRepository.findById(ATTACHMENT_ID)).thenReturn(Optional.empty());

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(ATTACHMENT_ID)).contains("does not exist");
        verify(assetRepository, times(1)).findById(ATTACHMENT_ID);
        verifyNoMoreInteractions(assetRepository);
        verifyNoInteractions(messageRepository);
    }

    @Test
    @DisplayName("handle(DeleteAttachmentCommand) -> wraps deleteById failure in RuntimeException (AAA)")
    void handleDeleteDeleteFailure() {
        // Arrange
        var command = new DeleteAssetCommand(ATTACHMENT_ID);
        Asset mockAsset = mock(Asset.class);
        when(mockAsset.getMessageId()).thenReturn(10L);
        Message mockMessage = mock(Message.class);
        when(assetRepository.findById(ATTACHMENT_ID)).thenReturn(Optional.of(mockAsset));
        when(messageRepository.findById(10L)).thenReturn(Optional.of(mockMessage));
        doThrow(new RuntimeException("fk")).when(messageRepository).save(any(Message.class));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error deleting Asset").contains("fk");
        verify(assetRepository, times(1)).findById(ATTACHMENT_ID);
        verify(messageRepository, times(1)).findById(10L);
        verify(mockMessage, times(1)).removeAttachment(ATTACHMENT_ID);
        verify(messageRepository, times(1)).save(mockMessage);
        verifyNoMoreInteractions(assetRepository, messageRepository);
    }
}
