package pe.edu.upc.soft.work.platform.worker.forum.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;
import pe.edu.upc.soft.work.platform.worker.forum.application.internal.outboundservices.acl.ExternalDashboardServiceFromWorkerForum;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Thread;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteThreadCommand;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.ThreadRepository;
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
class ThreadCommandServiceImplTest {

    private static final Long THREAD_ID = 71L;

    @Mock
    private ThreadRepository threadRepository;
    @Mock
    private ExternalDashboardServiceFromWorkerForum externalDashboardServiceFromWorkerForum;

    @InjectMocks
    private ThreadCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateThreadCommand) -> creates Thread when company guard passes (AAA)")
    void handleCreateSuccess() {
        // Arrange
        var command = WorkerForumCommandFixtures.validCreateThreadCommand();
        // NOTE (source quirk): the service calls existsCompanyById with the area-company id
        when(externalDashboardServiceFromWorkerForum.existsCompanyById(WorkerForumCommandFixtures.VALID_AREA_COMPANY_ID))
                .thenReturn(true);
        when(threadRepository.save(any(Thread.class))).thenAnswer(inv -> {
            Thread t = inv.getArgument(0);
            ReflectionTestUtils.setId(t, THREAD_ID);
            return t;
        });

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(THREAD_ID);
        verify(externalDashboardServiceFromWorkerForum, times(1))
                .existsCompanyById(WorkerForumCommandFixtures.VALID_AREA_COMPANY_ID);
        verify(threadRepository, times(1)).save(any(Thread.class));
        verifyNoMoreInteractions(externalDashboardServiceFromWorkerForum, threadRepository);
    }

    @Test
    @DisplayName("handle(CreateThreadCommand) -> throws NotFoundArgumentException when company guard fails (AAA)")
    void handleCreateMissingCompany() {
        // Arrange
        var command = WorkerForumCommandFixtures.validCreateThreadCommand();
        when(externalDashboardServiceFromWorkerForum.existsCompanyById(WorkerForumCommandFixtures.VALID_AREA_COMPANY_ID))
                .thenReturn(false);

        // Act + Assert
        NotFoundArgumentException ex = assertThrows(NotFoundArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Company ID: " + WorkerForumCommandFixtures.VALID_AREA_COMPANY_ID);
        verify(externalDashboardServiceFromWorkerForum, times(1))
                .existsCompanyById(WorkerForumCommandFixtures.VALID_AREA_COMPANY_ID);
        verifyNoMoreInteractions(externalDashboardServiceFromWorkerForum);
        verifyNoInteractions(threadRepository);
    }

    @Test
    @DisplayName("handle(CreateThreadCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleCreateSaveFailure() {
        // Arrange
        var command = WorkerForumCommandFixtures.validCreateThreadCommand();
        when(externalDashboardServiceFromWorkerForum.existsCompanyById(WorkerForumCommandFixtures.VALID_AREA_COMPANY_ID))
                .thenReturn(true);
        when(threadRepository.save(any(Thread.class))).thenThrow(new RuntimeException("db"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error creating Thread").contains("db");
        verify(externalDashboardServiceFromWorkerForum, times(1))
                .existsCompanyById(WorkerForumCommandFixtures.VALID_AREA_COMPANY_ID);
        verify(threadRepository, times(1)).save(any(Thread.class));
        verifyNoMoreInteractions(externalDashboardServiceFromWorkerForum, threadRepository);
    }

    @Test
    @DisplayName("handle(UpdateThreadCommand) -> returns Optional with updated Thread when present (AAA)")
    void handleUpdateSuccess() {
        // Arrange
        var existing = new Thread(WorkerForumCommandFixtures.validCreateThreadCommand());
        ReflectionTestUtils.setId(existing, THREAD_ID);
        var command = WorkerForumCommandFixtures.updateThreadCommand(THREAD_ID);
        when(threadRepository.existsById(THREAD_ID)).thenReturn(true);
        when(threadRepository.findById(THREAD_ID)).thenReturn(Optional.of(existing));
        when(threadRepository.save(existing)).thenReturn(existing);

        // Act
        Optional<Thread> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo(WorkerForumCommandFixtures.VALID_THREAD_TITLE);
        verify(threadRepository, times(1)).existsById(THREAD_ID);
        verify(threadRepository, times(1)).findById(THREAD_ID);
        verify(threadRepository, times(1)).save(existing);
        verifyNoMoreInteractions(threadRepository);
        verifyNoInteractions(externalDashboardServiceFromWorkerForum);
    }

    @Test
    @DisplayName("handle(UpdateThreadCommand) -> throws RuntimeException when id does not exist (AAA)")
    void handleUpdateMissing() {
        // Arrange
        var command = WorkerForumCommandFixtures.updateThreadCommand(THREAD_ID);
        when(threadRepository.existsById(THREAD_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(THREAD_ID)).contains("does not exist");
        verify(threadRepository, times(1)).existsById(THREAD_ID);
        verifyNoMoreInteractions(threadRepository);
        verifyNoInteractions(externalDashboardServiceFromWorkerForum);
    }

    @Test
    @DisplayName("handle(UpdateThreadCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleUpdateSaveFailure() {
        // Arrange
        var existing = new Thread(WorkerForumCommandFixtures.validCreateThreadCommand());
        ReflectionTestUtils.setId(existing, THREAD_ID);
        var command = WorkerForumCommandFixtures.updateThreadCommand(THREAD_ID);
        when(threadRepository.existsById(THREAD_ID)).thenReturn(true);
        when(threadRepository.findById(THREAD_ID)).thenReturn(Optional.of(existing));
        when(threadRepository.save(existing)).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error updating Thread").contains("boom");
        verify(threadRepository, times(1)).existsById(THREAD_ID);
        verify(threadRepository, times(1)).findById(THREAD_ID);
        verify(threadRepository, times(1)).save(existing);
        verifyNoMoreInteractions(threadRepository);
        verifyNoInteractions(externalDashboardServiceFromWorkerForum);
    }

    @Test
    @DisplayName("handle(DeleteThreadCommand) -> deletes when present (AAA)")
    void handleDeleteSuccess() {
        // Arrange
        var command = new DeleteThreadCommand(THREAD_ID);
        when(threadRepository.existsById(THREAD_ID)).thenReturn(true);

        // Act
        service.handle(command);

        // Assert
        verify(threadRepository, times(1)).existsById(THREAD_ID);
        verify(threadRepository, times(1)).deleteById(THREAD_ID);
        verifyNoMoreInteractions(threadRepository);
        verifyNoInteractions(externalDashboardServiceFromWorkerForum);
    }

    @Test
    @DisplayName("handle(DeleteThreadCommand) -> throws RuntimeException when id is absent (AAA)")
    void handleDeleteMissing() {
        // Arrange
        var command = new DeleteThreadCommand(THREAD_ID);
        when(threadRepository.existsById(THREAD_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(THREAD_ID)).contains("does not exist");
        verify(threadRepository, times(1)).existsById(THREAD_ID);
        verify(threadRepository, never()).deleteById(any(Long.class));
        verifyNoMoreInteractions(threadRepository);
        verifyNoInteractions(externalDashboardServiceFromWorkerForum);
    }

    @Test
    @DisplayName("handle(DeleteThreadCommand) -> wraps deleteById failure in RuntimeException (AAA)")
    void handleDeleteDeleteFailure() {
        // Arrange
        var command = new DeleteThreadCommand(THREAD_ID);
        when(threadRepository.existsById(THREAD_ID)).thenReturn(true);
        doThrow(new RuntimeException("fk")).when(threadRepository).deleteById(THREAD_ID);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error deleting Thread").contains("fk");
        verify(threadRepository, times(1)).existsById(THREAD_ID);
        verify(threadRepository, times(1)).deleteById(THREAD_ID);
        verifyNoMoreInteractions(threadRepository);
        verifyNoInteractions(externalDashboardServiceFromWorkerForum);
    }
}
