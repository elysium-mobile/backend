package pe.edu.upc.soft.work.platform.worker.forum.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;
import pe.edu.upc.soft.work.platform.worker.forum.application.internal.outboundservices.acl.ExternalDashboardServiceFromWorkerForum;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Forum;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteForumCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.events.ForumCreatedEvent;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.CategoryRepository;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.ForumRepository;
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
class ForumCommandServiceImplTest {

    private static final Long FORUM_ID = 51L;

    @Mock
    private ForumRepository forumRepository;
    @Mock
    private ExternalDashboardServiceFromWorkerForum externalDashboardServiceFromWorkerForum;

    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ForumCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateForumCommand) -> creates Forum when company exists (AAA)")
    void handleCreateSuccess() {
        // Arrange
        var command = WorkerForumCommandFixtures.validCreateForumCommand();
        when(externalDashboardServiceFromWorkerForum.existsCompanyById(any())).thenReturn(true);
        when(forumRepository.save(any(Forum.class))).thenAnswer(inv -> {
            Forum f = inv.getArgument(0);
            ReflectionTestUtils.setId(f, FORUM_ID);
            return f;
        });

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(FORUM_ID);
        verify(eventPublisher).publishEvent(any(ForumCreatedEvent.class));
        verify(forumRepository).save(any(Forum.class));
        verifyNoMoreInteractions(externalDashboardServiceFromWorkerForum, forumRepository, eventPublisher);
    }

    @Test
    @DisplayName("handle(CreateForumCommand) -> throws NotFoundArgumentException when company is missing (AAA)")
    void handleCreateMissingCompany() {
        // Arrange
        var command = WorkerForumCommandFixtures.validCreateForumCommand();
        when(externalDashboardServiceFromWorkerForum.existsCompanyById(WorkerForumCommandFixtures.VALID_COMPANY_ID))
                .thenReturn(false);

        // Act + Assert
        NotFoundArgumentException ex = assertThrows(NotFoundArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Company ID: " + WorkerForumCommandFixtures.VALID_COMPANY_ID);
        verify(externalDashboardServiceFromWorkerForum, times(1))
                .existsCompanyById(WorkerForumCommandFixtures.VALID_COMPANY_ID);
        verifyNoMoreInteractions(externalDashboardServiceFromWorkerForum);
        verifyNoInteractions(forumRepository);
    }

    @Test
    @DisplayName("handle(CreateForumCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleCreateSaveFailure() {
        // Arrange
        var command = WorkerForumCommandFixtures.validCreateForumCommand();
        when(externalDashboardServiceFromWorkerForum.existsCompanyById(WorkerForumCommandFixtures.VALID_COMPANY_ID))
            .thenReturn(true);
        when(forumRepository.save(any(Forum.class))).thenThrow(new RuntimeException("db"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error creating Forum").contains("db");
        verify(externalDashboardServiceFromWorkerForum, times(1))
            .existsCompanyById(WorkerForumCommandFixtures.VALID_COMPANY_ID);
        verify(eventPublisher, times(1)).publishEvent(any(ForumCreatedEvent.class));
        verify(forumRepository, times(1)).save(any(Forum.class));
        verifyNoMoreInteractions(externalDashboardServiceFromWorkerForum, forumRepository, eventPublisher);
    }

    @Test
    @DisplayName("handle(UpdateForumCommand) -> returns Optional with updated Forum when present (AAA)")
    void handleUpdateSuccess() {
        // Arrange
        var existing = new Forum(WorkerForumCommandFixtures.validCreateForumCommand());
        ReflectionTestUtils.setId(existing, FORUM_ID);
        var command = WorkerForumCommandFixtures.updateForumCommand(FORUM_ID);
        when(forumRepository.existsById(FORUM_ID)).thenReturn(true);
        when(forumRepository.findById(FORUM_ID)).thenReturn(Optional.of(existing));
        when(forumRepository.save(existing)).thenReturn(existing);

        // Act
        Optional<Forum> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo(WorkerForumCommandFixtures.VALID_FORUM_TITLE);
        verify(forumRepository, times(1)).existsById(FORUM_ID);
        verify(forumRepository, times(1)).findById(FORUM_ID);
        verify(forumRepository, times(1)).save(existing);
        verifyNoMoreInteractions(forumRepository);
        verifyNoInteractions(externalDashboardServiceFromWorkerForum);
    }

    @Test
    @DisplayName("handle(UpdateForumCommand) -> throws RuntimeException when id does not exist (AAA)")
    void handleUpdateMissing() {
        // Arrange
        var command = WorkerForumCommandFixtures.updateForumCommand(FORUM_ID);
        when(forumRepository.existsById(FORUM_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(FORUM_ID)).contains("does not exist");
        verify(forumRepository, times(1)).existsById(FORUM_ID);
        verifyNoMoreInteractions(forumRepository);
        verifyNoInteractions(externalDashboardServiceFromWorkerForum);
    }

    @Test
    @DisplayName("handle(UpdateForumCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleUpdateSaveFailure() {
        // Arrange
        var existing = new Forum(WorkerForumCommandFixtures.validCreateForumCommand());
        ReflectionTestUtils.setId(existing, FORUM_ID);
        var command = WorkerForumCommandFixtures.updateForumCommand(FORUM_ID);
        when(forumRepository.existsById(FORUM_ID)).thenReturn(true);
        when(forumRepository.findById(FORUM_ID)).thenReturn(Optional.of(existing));
        when(forumRepository.save(existing)).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error updating Forum").contains("boom");
        verify(forumRepository, times(1)).existsById(FORUM_ID);
        verify(forumRepository, times(1)).findById(FORUM_ID);
        verify(forumRepository, times(1)).save(existing);
        verifyNoMoreInteractions(forumRepository);
        verifyNoInteractions(externalDashboardServiceFromWorkerForum);
    }

    @Test
    @DisplayName("handle(DeleteForumCommand) -> deletes when present (AAA)")
    void handleDeleteSuccess() {
        // Arrange
        var command = new DeleteForumCommand(FORUM_ID);
        when(forumRepository.existsById(FORUM_ID)).thenReturn(true);

        // Act
        service.handle(command);

        // Assert
        verify(forumRepository, times(1)).existsById(FORUM_ID);
        verify(forumRepository, times(1)).deleteById(FORUM_ID);
        verifyNoMoreInteractions(forumRepository);
        verifyNoInteractions(externalDashboardServiceFromWorkerForum);
    }

    @Test
    @DisplayName("handle(DeleteForumCommand) -> throws RuntimeException when id is absent (AAA)")
    void handleDeleteMissing() {
        // Arrange
        var command = new DeleteForumCommand(FORUM_ID);
        when(forumRepository.existsById(FORUM_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(FORUM_ID)).contains("does not exist");
        verify(forumRepository, times(1)).existsById(FORUM_ID);
        verify(forumRepository, never()).deleteById(any(Long.class));
        verifyNoMoreInteractions(forumRepository);
        verifyNoInteractions(externalDashboardServiceFromWorkerForum);
    }

    @Test
    @DisplayName("handle(DeleteForumCommand) -> wraps deleteById failure in RuntimeException (AAA)")
    void handleDeleteDeleteFailure() {
        // Arrange
        var command = new DeleteForumCommand(FORUM_ID);
        when(forumRepository.existsById(FORUM_ID)).thenReturn(true);
        doThrow(new RuntimeException("fk")).when(forumRepository).deleteById(FORUM_ID);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error deleting Forum").contains("fk");
        verify(forumRepository, times(1)).existsById(FORUM_ID);
        verify(forumRepository, times(1)).deleteById(FORUM_ID);
        verifyNoMoreInteractions(forumRepository);
        verifyNoInteractions(externalDashboardServiceFromWorkerForum);
    }
}
