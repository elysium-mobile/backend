package pe.edu.upc.soft.work.platform.dashboard.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.DeleteUnitOfWorkCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.UnitOfWork;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.UnitOfWorkRepository;
import pe.edu.upc.soft.work.platform.dashboard.test.fixtures.DashboardCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UnitOfWorkCommandServiceImplTest {

    private static final Long UOW_ID = 33L;

    @Mock
    private UnitOfWorkRepository unitofworkRepository;

    @InjectMocks
    private UnitOfWorkCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateUnitOfWorkCommand) -> creates UnitOfWork and returns generated id (AAA)")
    void handleCreateSuccess() {
        // Arrange
        var command = DashboardCommandFixtures.validCreateUnitOfWorkCommand();
        when(unitofworkRepository.save(any(UnitOfWork.class))).thenAnswer(inv -> {
            UnitOfWork u = inv.getArgument(0);
            ReflectionTestUtils.setId(u, UOW_ID);
            return u;
        });

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(UOW_ID);
        verify(unitofworkRepository, times(1)).save(any(UnitOfWork.class));
        verifyNoMoreInteractions(unitofworkRepository);
    }

    @Test
    @DisplayName("handle(CreateUnitOfWorkCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleCreateSaveFailure() {
        // Arrange
        var command = DashboardCommandFixtures.validCreateUnitOfWorkCommand();
        when(unitofworkRepository.save(any(UnitOfWork.class))).thenThrow(new RuntimeException("db"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error creating UnitOfWork").contains("db");
        verify(unitofworkRepository, times(1)).save(any(UnitOfWork.class));
        verifyNoMoreInteractions(unitofworkRepository);
    }

    @Test
    @DisplayName("handle(UpdateUnitOfWorkCommand) -> returns Optional with updated UnitOfWork when present (AAA)")
    void handleUpdateSuccess() {
        // Arrange
        var existing = new UnitOfWork(DashboardCommandFixtures.validCreateUnitOfWorkCommand());
        ReflectionTestUtils.setId(existing, UOW_ID);
        var command = DashboardCommandFixtures.updateUnitOfWorkCommand(UOW_ID);
        when(unitofworkRepository.existsById(UOW_ID)).thenReturn(true);
        when(unitofworkRepository.findById(UOW_ID)).thenReturn(Optional.of(existing));
        when(unitofworkRepository.save(existing)).thenReturn(existing);

        // Act
        Optional<UnitOfWork> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(DashboardCommandFixtures.VALID_UNIT_OF_WORK_NAME);
        verify(unitofworkRepository, times(1)).existsById(UOW_ID);
        verify(unitofworkRepository, times(1)).findById(UOW_ID);
        verify(unitofworkRepository, times(1)).save(existing);
        verifyNoMoreInteractions(unitofworkRepository);
    }

    @Test
    @DisplayName("handle(UpdateUnitOfWorkCommand) -> throws RuntimeException when id does not exist (AAA)")
    void handleUpdateMissing() {
        // Arrange
        var command = DashboardCommandFixtures.updateUnitOfWorkCommand(UOW_ID);
        when(unitofworkRepository.existsById(UOW_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(UOW_ID)).contains("does not exist");
        verify(unitofworkRepository, times(1)).existsById(UOW_ID);
        verifyNoMoreInteractions(unitofworkRepository);
    }

    @Test
    @DisplayName("handle(UpdateUnitOfWorkCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleUpdateSaveFailure() {
        // Arrange
        var existing = new UnitOfWork(DashboardCommandFixtures.validCreateUnitOfWorkCommand());
        ReflectionTestUtils.setId(existing, UOW_ID);
        var command = DashboardCommandFixtures.updateUnitOfWorkCommand(UOW_ID);
        when(unitofworkRepository.existsById(UOW_ID)).thenReturn(true);
        when(unitofworkRepository.findById(UOW_ID)).thenReturn(Optional.of(existing));
        when(unitofworkRepository.save(existing)).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error updating UnitOfWork").contains("boom");
        verify(unitofworkRepository, times(1)).existsById(UOW_ID);
        verify(unitofworkRepository, times(1)).findById(UOW_ID);
        verify(unitofworkRepository, times(1)).save(existing);
        verifyNoMoreInteractions(unitofworkRepository);
    }

    @Test
    @DisplayName("handle(DeleteUnitOfWorkCommand) -> deletes when present (AAA)")
    void handleDeleteSuccess() {
        // Arrange
        var command = new DeleteUnitOfWorkCommand(UOW_ID);
        when(unitofworkRepository.existsById(UOW_ID)).thenReturn(true);

        // Act
        service.handle(command);

        // Assert
        verify(unitofworkRepository, times(1)).existsById(UOW_ID);
        verify(unitofworkRepository, times(1)).deleteById(UOW_ID);
        verifyNoMoreInteractions(unitofworkRepository);
    }

    @Test
    @DisplayName("handle(DeleteUnitOfWorkCommand) -> throws RuntimeException when id is absent (AAA)")
    void handleDeleteMissing() {
        // Arrange
        var command = new DeleteUnitOfWorkCommand(UOW_ID);
        when(unitofworkRepository.existsById(UOW_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(UOW_ID)).contains("does not exist");
        verify(unitofworkRepository, times(1)).existsById(UOW_ID);
        verify(unitofworkRepository, never()).deleteById(any(Long.class));
        verifyNoMoreInteractions(unitofworkRepository);
    }

    @Test
    @DisplayName("handle(DeleteUnitOfWorkCommand) -> wraps deleteById failure in RuntimeException (AAA)")
    void handleDeleteDeleteFailure() {
        // Arrange
        var command = new DeleteUnitOfWorkCommand(UOW_ID);
        when(unitofworkRepository.existsById(UOW_ID)).thenReturn(true);
        doThrow(new RuntimeException("fk")).when(unitofworkRepository).deleteById(UOW_ID);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error deleting UnitOfWork").contains("fk");
        verify(unitofworkRepository, times(1)).existsById(UOW_ID);
        verify(unitofworkRepository, times(1)).deleteById(UOW_ID);
        verifyNoMoreInteractions(unitofworkRepository);
    }
}
