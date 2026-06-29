package pe.edu.upc.soft.work.platform.profile.performance.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import pe.edu.upc.soft.work.platform.profile.performance.application.internal.outboundservices.acl.ExternalIamServiceFromProfilePerformance;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.aggregates.CommentEmployee;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.aggregates.Performance;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.DeleteCommentEmployeeCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.events.CommentEmployeeAddedEvent;
import pe.edu.upc.soft.work.platform.profile.performance.infrastructure.persistence.jpa.repositories.CommentEmployeeRepository;
import pe.edu.upc.soft.work.platform.profile.performance.infrastructure.persistence.jpa.repositories.PerformanceRepository;
import pe.edu.upc.soft.work.platform.profile.performance.test.fixtures.ProfilePerformanceCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;
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
class CommentEmployeeCommandServiceImplTest {

    private static final Long COMMENT_ID = 13L;

    @Mock
    private CommentEmployeeRepository commentemployeeRepository;
    @Mock
    private ExternalIamServiceFromProfilePerformance externalIamServiceFromProfilePerformance;

    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private PerformanceRepository performanceRepository;

    @InjectMocks
    private CommentEmployeeCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateCommentEmployeeCommand) -> creates CommentEmployee when RRHH profile exists (AAA)")
    void handleCreateSuccess() {
        // Arrange
        var command = ProfilePerformanceCommandFixtures.validCreateCommentEmployeeCommand();
        var performance = new Performance();

        when(externalIamServiceFromProfilePerformance.existsRRHHProfileById(any())).thenReturn(true);
        when(performanceRepository.existsById(any())).thenReturn(true);
        when(performanceRepository.findById(any())).thenReturn(Optional.of(performance));

        when(commentemployeeRepository.save(any(CommentEmployee.class))).thenAnswer(inv -> {
            CommentEmployee c = inv.getArgument(0);
            ReflectionTestUtils.setId(c, COMMENT_ID);
            return c;
        });

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(COMMENT_ID);
        verify(eventPublisher).publishEvent(any(CommentEmployeeAddedEvent.class));
        verify(performanceRepository).existsById(any());
        verify(performanceRepository).save(any(Performance.class));
        verify(commentemployeeRepository).save(any(CommentEmployee.class));
        verifyNoMoreInteractions(eventPublisher, performanceRepository, commentemployeeRepository, externalIamServiceFromProfilePerformance); }

    @Test
    @DisplayName("handle(CreateCommentEmployeeCommand) -> throws NotFoundArgumentException when RRHH profile is missing (AAA)")
    void handleCreateMissingRRHH() {
        // Arrange
        var command = ProfilePerformanceCommandFixtures.validCreateCommentEmployeeCommand();
        when(externalIamServiceFromProfilePerformance.existsRRHHProfileById(ProfilePerformanceCommandFixtures.VALID_RRHH_PROFILE_ID))
                .thenReturn(false);

        // Act + Assert
        NotFoundArgumentException ex = assertThrows(NotFoundArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("RRHH Profile ID: " + ProfilePerformanceCommandFixtures.VALID_RRHH_PROFILE_ID);
        verify(externalIamServiceFromProfilePerformance, times(1))
                .existsRRHHProfileById(ProfilePerformanceCommandFixtures.VALID_RRHH_PROFILE_ID);
        verifyNoMoreInteractions(externalIamServiceFromProfilePerformance);
        verifyNoInteractions(commentemployeeRepository);
    }

    @Test
    @DisplayName("handle(CreateCommentEmployeeCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleCreateSaveFailure() {
        var command = ProfilePerformanceCommandFixtures.validCreateCommentEmployeeCommand();
        var performance = new Performance();
        when(externalIamServiceFromProfilePerformance.existsRRHHProfileById(any())).thenReturn(true);
        when(performanceRepository.existsById(any())).thenReturn(true);
        when(performanceRepository.findById(any())).thenReturn(Optional.of(performance));
        when(commentemployeeRepository.save(any(CommentEmployee.class))).thenThrow(new RuntimeException("db"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error creating CommentEmployee").contains("db");
        verify(externalIamServiceFromProfilePerformance).existsRRHHProfileById(any());
        verify(performanceRepository).existsById(any());
        verify(performanceRepository).findById(any());
        verify(commentemployeeRepository).save(any(CommentEmployee.class));}

    @Test
    @DisplayName("handle(UpdateCommentEmployeeCommand) -> returns Optional with updated CommentEmployee when present (AAA)")
    void handleUpdateSuccess() {
        // Arrange
        var existing = new CommentEmployee(ProfilePerformanceCommandFixtures.validCreateCommentEmployeeCommand());
        ReflectionTestUtils.setId(existing, COMMENT_ID);
        var command = ProfilePerformanceCommandFixtures.updateCommentEmployeeCommand(COMMENT_ID);
        when(performanceRepository.existsById(command.performanceId())).thenReturn(true);
        when(commentemployeeRepository.existsById(COMMENT_ID)).thenReturn(true);
        when(commentemployeeRepository.findById(COMMENT_ID)).thenReturn(Optional.of(existing));
        when(commentemployeeRepository.save(any(CommentEmployee.class))).thenReturn(existing);

        // Act
        Optional<CommentEmployee> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo(ProfilePerformanceCommandFixtures.VALID_COMMENT_TITLE);
        assertThat(result.get().getContent()).isEqualTo(ProfilePerformanceCommandFixtures.VALID_COMMENT_CONTENT);
        verify(performanceRepository, times(1)).existsById(command.performanceId());
        verify(commentemployeeRepository, times(1)).existsById(COMMENT_ID);
        verify(commentemployeeRepository, times(1)).findById(COMMENT_ID);
        verify(commentemployeeRepository, times(1)).save(any(CommentEmployee.class));
        verifyNoMoreInteractions(commentemployeeRepository, performanceRepository);
        verifyNoInteractions(externalIamServiceFromProfilePerformance, eventPublisher);
    }

    @Test
    @DisplayName("handle(UpdateCommentEmployeeCommand) -> throws RuntimeException when id does not exist (AAA)")
    void handleUpdateMissing() {
        // Arrange
        var command = ProfilePerformanceCommandFixtures.updateCommentEmployeeCommand(COMMENT_ID);
        when(performanceRepository.existsById(command.performanceId())).thenReturn(true);
        when(commentemployeeRepository.existsById(COMMENT_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(COMMENT_ID)).contains("does not exist");
        verify(performanceRepository, times(1)).existsById(command.performanceId());
        verify(commentemployeeRepository, times(1)).existsById(COMMENT_ID);
        verifyNoMoreInteractions(commentemployeeRepository, performanceRepository);
        verifyNoInteractions(externalIamServiceFromProfilePerformance, eventPublisher);
    }

    @Test
    @DisplayName("handle(UpdateCommentEmployeeCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleUpdateSaveFailure() {
        // Arrange
        var existing = new CommentEmployee(ProfilePerformanceCommandFixtures.validCreateCommentEmployeeCommand());
        ReflectionTestUtils.setId(existing, COMMENT_ID);
        var command = ProfilePerformanceCommandFixtures.updateCommentEmployeeCommand(COMMENT_ID);
        when(performanceRepository.existsById(command.performanceId())).thenReturn(true);
        when(commentemployeeRepository.existsById(COMMENT_ID)).thenReturn(true);
        when(commentemployeeRepository.findById(COMMENT_ID)).thenReturn(Optional.of(existing));
        when(commentemployeeRepository.save(any(CommentEmployee.class))).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error updating CommentEmployee").contains("boom");
        verify(performanceRepository, times(1)).existsById(command.performanceId());
        verify(commentemployeeRepository, times(1)).existsById(COMMENT_ID);
        verify(commentemployeeRepository, times(1)).findById(COMMENT_ID);
        verify(commentemployeeRepository, times(1)).save(any(CommentEmployee.class));
        verifyNoMoreInteractions(commentemployeeRepository, performanceRepository);
        verifyNoInteractions(externalIamServiceFromProfilePerformance, eventPublisher);
    }

    @Test
    @DisplayName("handle(DeleteCommentEmployeeCommand) -> deletes when present (AAA)")
    void handleDeleteSuccess() {
        // Arrange
        var command = new DeleteCommentEmployeeCommand(COMMENT_ID);
        when(commentemployeeRepository.existsById(COMMENT_ID)).thenReturn(true);

        // Act
        service.handle(command);

        // Assert
        verify(commentemployeeRepository, times(1)).existsById(COMMENT_ID);
        verify(commentemployeeRepository, times(1)).deleteById(COMMENT_ID);
        verifyNoMoreInteractions(commentemployeeRepository);
        verifyNoInteractions(externalIamServiceFromProfilePerformance);
    }

    @Test
    @DisplayName("handle(DeleteCommentEmployeeCommand) -> throws RuntimeException when id is absent (AAA)")
    void handleDeleteMissing() {
        // Arrange
        var command = new DeleteCommentEmployeeCommand(COMMENT_ID);
        when(commentemployeeRepository.existsById(COMMENT_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(COMMENT_ID)).contains("does not exist");
        verify(commentemployeeRepository, times(1)).existsById(COMMENT_ID);
        verify(commentemployeeRepository, never()).deleteById(any(Long.class));
        verifyNoMoreInteractions(commentemployeeRepository);
        verifyNoInteractions(externalIamServiceFromProfilePerformance);
    }

    @Test
    @DisplayName("handle(DeleteCommentEmployeeCommand) -> wraps deleteById failure in RuntimeException (AAA)")
    void handleDeleteDeleteFailure() {
        // Arrange
        var command = new DeleteCommentEmployeeCommand(COMMENT_ID);
        when(commentemployeeRepository.existsById(COMMENT_ID)).thenReturn(true);
        doThrow(new RuntimeException("fk")).when(commentemployeeRepository).deleteById(COMMENT_ID);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error deleting CommentEmployee").contains("fk");
        verify(commentemployeeRepository, times(1)).existsById(COMMENT_ID);
        verify(commentemployeeRepository, times(1)).deleteById(COMMENT_ID);
        verifyNoMoreInteractions(commentemployeeRepository);
        verifyNoInteractions(externalIamServiceFromProfilePerformance);
    }
}
