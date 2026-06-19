package pe.edu.upc.soft.work.platform.profile.performance.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import pe.edu.upc.soft.work.platform.profile.performance.application.internal.outboundservices.acl.ExternalIamServiceFromProfilePerformance;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.aggregates.Performance;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.DeletePerformanceCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.events.PerformanceRegisteredEvent;
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
class PerformanceCommandServiceImplTest {

    private static final Long PERFORMANCE_ID = 23L;

    @Mock
    private PerformanceRepository performanceRepository;
    @Mock
    private ExternalIamServiceFromProfilePerformance externalIamServiceFromProfilePerformance;

    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private CommentEmployeeRepository commentEmployeeRepository;

    @InjectMocks
    private PerformanceCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreatePerformanceCommand) -> creates Performance when employee profile exists (AAA)")
    void handleCreateSuccess() {
        // Arrange
        var command = ProfilePerformanceCommandFixtures.validCreatePerformanceCommand();
        when(externalIamServiceFromProfilePerformance.existsEmployeeProfileById(any())).thenReturn(true);
        when(performanceRepository.save(any(Performance.class))).thenAnswer(inv -> {
            Performance p = inv.getArgument(0);
            ReflectionTestUtils.setId(p, PERFORMANCE_ID);
            return p;
        });

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(PERFORMANCE_ID);
        verify(eventPublisher, times(1)).publishEvent(any(PerformanceRegisteredEvent.class));
        verify(performanceRepository, times(1)).save(any(Performance.class));
        verifyNoMoreInteractions(externalIamServiceFromProfilePerformance, performanceRepository, eventPublisher);
    }

    @Test
    @DisplayName("handle(CreatePerformanceCommand) -> throws NotFoundArgumentException when employee profile is missing (AAA)")
    void handleCreateMissingEmployeeProfile() {
        // Arrange
        var command = ProfilePerformanceCommandFixtures.validCreatePerformanceCommand();
        when(externalIamServiceFromProfilePerformance.existsEmployeeProfileById(
                ProfilePerformanceCommandFixtures.VALID_EMPLOYEE_PROFILE_ID)).thenReturn(false);

        // Act + Assert
        NotFoundArgumentException ex = assertThrows(NotFoundArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Employee Profile ID: " + ProfilePerformanceCommandFixtures.VALID_EMPLOYEE_PROFILE_ID);
        verify(externalIamServiceFromProfilePerformance, times(1))
                .existsEmployeeProfileById(ProfilePerformanceCommandFixtures.VALID_EMPLOYEE_PROFILE_ID);
        verifyNoMoreInteractions(externalIamServiceFromProfilePerformance);
        verifyNoInteractions(performanceRepository);
    }

    @Test
    @DisplayName("handle(CreatePerformanceCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleCreateSaveFailure() {
        // Arrange
        var command = ProfilePerformanceCommandFixtures.validCreatePerformanceCommand();
        when(externalIamServiceFromProfilePerformance.existsEmployeeProfileById(
            ProfilePerformanceCommandFixtures.VALID_EMPLOYEE_PROFILE_ID)).thenReturn(true);
        when(performanceRepository.save(any(Performance.class))).thenThrow(new RuntimeException("db"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error creating Performance").contains("db");
        verify(externalIamServiceFromProfilePerformance, times(1))
            .existsEmployeeProfileById(ProfilePerformanceCommandFixtures.VALID_EMPLOYEE_PROFILE_ID);
        verify(eventPublisher, times(1)).publishEvent(any(PerformanceRegisteredEvent.class));
        verify(performanceRepository, times(1)).save(any(Performance.class));
        verifyNoMoreInteractions(externalIamServiceFromProfilePerformance, performanceRepository, eventPublisher);
    }

    @Test
    @DisplayName("handle(UpdatePerformanceCommand) -> returns Optional with updated Performance when present (AAA)")
    void handleUpdateSuccess() {
        // Arrange
        var existing = new Performance(ProfilePerformanceCommandFixtures.validCreatePerformanceCommand());
        ReflectionTestUtils.setId(existing, PERFORMANCE_ID);
        var command = ProfilePerformanceCommandFixtures.updatePerformanceCommand(PERFORMANCE_ID);
        when(performanceRepository.existsById(PERFORMANCE_ID)).thenReturn(true);
        when(performanceRepository.findById(PERFORMANCE_ID)).thenReturn(Optional.of(existing));
        when(performanceRepository.save(existing)).thenReturn(existing);

        // Act
        Optional<Performance> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getClassification()).isEqualTo(ProfilePerformanceCommandFixtures.VALID_CLASSIFICATION);
        verify(performanceRepository, times(1)).existsById(PERFORMANCE_ID);
        verify(performanceRepository, times(1)).findById(PERFORMANCE_ID);
        verify(performanceRepository, times(1)).save(existing);
        verifyNoMoreInteractions(performanceRepository);
        verifyNoInteractions(externalIamServiceFromProfilePerformance);
    }

    @Test
    @DisplayName("handle(UpdatePerformanceCommand) -> throws RuntimeException when id does not exist (AAA)")
    void handleUpdateMissing() {
        // Arrange
        var command = ProfilePerformanceCommandFixtures.updatePerformanceCommand(PERFORMANCE_ID);
        when(performanceRepository.existsById(PERFORMANCE_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(PERFORMANCE_ID)).contains("does not exist");
        verify(performanceRepository, times(1)).existsById(PERFORMANCE_ID);
        verifyNoMoreInteractions(performanceRepository);
        verifyNoInteractions(externalIamServiceFromProfilePerformance);
    }

    @Test
    @DisplayName("handle(UpdatePerformanceCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleUpdateSaveFailure() {
        // Arrange
        var existing = new Performance(ProfilePerformanceCommandFixtures.validCreatePerformanceCommand());
        ReflectionTestUtils.setId(existing, PERFORMANCE_ID);
        var command = ProfilePerformanceCommandFixtures.updatePerformanceCommand(PERFORMANCE_ID);
        when(performanceRepository.existsById(PERFORMANCE_ID)).thenReturn(true);
        when(performanceRepository.findById(PERFORMANCE_ID)).thenReturn(Optional.of(existing));
        when(performanceRepository.save(existing)).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error updating Performance").contains("boom");
        verify(performanceRepository, times(1)).existsById(PERFORMANCE_ID);
        verify(performanceRepository, times(1)).findById(PERFORMANCE_ID);
        verify(performanceRepository, times(1)).save(existing);
        verifyNoMoreInteractions(performanceRepository);
        verifyNoInteractions(externalIamServiceFromProfilePerformance);
    }

    @Test
    @DisplayName("handle(DeletePerformanceCommand) -> deletes when present (AAA)")
    void handleDeleteSuccess() {
        // Arrange
        var command = new DeletePerformanceCommand(PERFORMANCE_ID);
        when(performanceRepository.existsById(PERFORMANCE_ID)).thenReturn(true);

        // Act
        service.handle(command);

        // Assert
        verify(performanceRepository, times(1)).existsById(PERFORMANCE_ID);
        verify(performanceRepository, times(1)).deleteById(PERFORMANCE_ID);
        verifyNoMoreInteractions(performanceRepository);
        verifyNoInteractions(externalIamServiceFromProfilePerformance);
    }

    @Test
    @DisplayName("handle(DeletePerformanceCommand) -> throws RuntimeException when id is absent (AAA)")
    void handleDeleteMissing() {
        // Arrange
        var command = new DeletePerformanceCommand(PERFORMANCE_ID);
        when(performanceRepository.existsById(PERFORMANCE_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(PERFORMANCE_ID)).contains("does not exist");
        verify(performanceRepository, times(1)).existsById(PERFORMANCE_ID);
        verify(performanceRepository, never()).deleteById(any(Long.class));
        verifyNoMoreInteractions(performanceRepository);
        verifyNoInteractions(externalIamServiceFromProfilePerformance);
    }

    @Test
    @DisplayName("handle(DeletePerformanceCommand) -> wraps deleteById failure in RuntimeException (AAA)")
    void handleDeleteDeleteFailure() {
        // Arrange
        var command = new DeletePerformanceCommand(PERFORMANCE_ID);
        when(performanceRepository.existsById(PERFORMANCE_ID)).thenReturn(true);
        doThrow(new RuntimeException("fk")).when(performanceRepository).deleteById(PERFORMANCE_ID);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error deleting Performance").contains("fk");
        verify(performanceRepository, times(1)).existsById(PERFORMANCE_ID);
        verify(performanceRepository, times(1)).deleteById(PERFORMANCE_ID);
        verifyNoMoreInteractions(performanceRepository);
        verifyNoInteractions(externalIamServiceFromProfilePerformance);
    }
}
