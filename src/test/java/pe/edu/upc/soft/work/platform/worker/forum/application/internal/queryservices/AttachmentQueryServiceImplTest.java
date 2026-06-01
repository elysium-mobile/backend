package pe.edu.upc.soft.work.platform.worker.forum.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Attachment;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAllAttachmentQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAttachmentByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.AttachmentRepository;
import pe.edu.upc.soft.work.platform.worker.forum.test.fixtures.WorkerForumCommandFixtures;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttachmentQueryServiceImplTest {

    @Mock
    private AttachmentRepository attachmentRepository;

    @InjectMocks
    private AttachmentQueryServiceImpl service;

    private static Attachment sample() {
        return new Attachment(WorkerForumCommandFixtures.validCreateAttachmentCommand());
    }

    @Test
    @DisplayName("handle(GetAllAttachmentQuery) -> returns list from repository (AAA)")
    void handleAllReturnsList() {
        // Arrange
        List<Attachment> attachments = List.of(sample(), sample());
        when(attachmentRepository.findAll()).thenReturn(attachments);

        // Act
        List<Attachment> result = service.handle(new GetAllAttachmentQuery());

        // Assert
        assertThat(result).hasSize(2).containsExactlyElementsOf(attachments);
        verify(attachmentRepository, times(1)).findAll();
        verifyNoMoreInteractions(attachmentRepository);
    }

    @Test
    @DisplayName("handle(GetAllAttachmentQuery) -> returns empty list when none exist (AAA)")
    void handleAllReturnsEmpty() {
        // Arrange
        when(attachmentRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Attachment> result = service.handle(new GetAllAttachmentQuery());

        // Assert
        assertThat(result).isEmpty();
        verify(attachmentRepository, times(1)).findAll();
        verifyNoMoreInteractions(attachmentRepository);
    }

    @Test
    @DisplayName("handle(GetAttachmentByIdQuery) -> returns Optional with Attachment when found (AAA)")
    void handleByIdReturnsPresent() {
        // Arrange
        var attachment = sample();
        when(attachmentRepository.findById(31L)).thenReturn(Optional.of(attachment));

        // Act
        Optional<Attachment> result = service.handle(new GetAttachmentByIdQuery(31L));

        // Assert
        assertThat(result).isPresent().containsSame(attachment);
        verify(attachmentRepository, times(1)).findById(31L);
        verifyNoMoreInteractions(attachmentRepository);
    }

    @Test
    @DisplayName("handle(GetAttachmentByIdQuery) -> returns Optional.empty when no Attachment found (AAA)")
    void handleByIdReturnsEmpty() {
        // Arrange
        when(attachmentRepository.findById(31L)).thenReturn(Optional.empty());

        // Act
        Optional<Attachment> result = service.handle(new GetAttachmentByIdQuery(31L));

        // Assert
        assertThat(result).isEmpty();
        verify(attachmentRepository, times(1)).findById(31L);
        verifyNoMoreInteractions(attachmentRepository);
    }
}
