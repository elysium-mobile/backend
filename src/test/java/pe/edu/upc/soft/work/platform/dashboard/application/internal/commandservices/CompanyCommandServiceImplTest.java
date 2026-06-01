package pe.edu.upc.soft.work.platform.dashboard.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates.Company;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.DeleteCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.CompanyRepository;
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
class CompanyCommandServiceImplTest {

    private static final Long COMPANY_ID = 12L;

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateCompanyCommand) -> creates Company and returns generated id (AAA)")
    void handleCreateSuccess() {
        // Arrange
        var command = DashboardCommandFixtures.validCreateCompanyCommand();
        ArgumentCaptor<Company> captor = ArgumentCaptor.forClass(Company.class);
        when(companyRepository.save(captor.capture())).thenAnswer(inv -> {
            Company c = inv.getArgument(0);
            ReflectionTestUtils.setId(c, COMPANY_ID);
            return c;
        });

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(COMPANY_ID);
        assertThat(captor.getValue().getName()).isEqualTo(DashboardCommandFixtures.VALID_COMPANY_NAME);
        assertThat(captor.getValue().getRUC()).isEqualTo(DashboardCommandFixtures.VALID_RUC);
        verify(companyRepository, times(1)).save(any(Company.class));
        verifyNoMoreInteractions(companyRepository);
    }

    @Test
    @DisplayName("handle(CreateCompanyCommand) -> wraps repository save failure in RuntimeException (AAA)")
    void handleCreateSaveFailure() {
        // Arrange
        var command = DashboardCommandFixtures.validCreateCompanyCommand();
        when(companyRepository.save(any(Company.class))).thenThrow(new RuntimeException("db down"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error creating Company").contains("db down");
        verify(companyRepository, times(1)).save(any(Company.class));
        verifyNoMoreInteractions(companyRepository);
    }

    @Test
    @DisplayName("handle(UpdateCompanyCommand) -> returns Optional with updated Company when id exists (AAA)")
    void handleUpdateSuccess() {
        // Arrange
        var existing = new Company(DashboardCommandFixtures.validCreateCompanyCommand());
        ReflectionTestUtils.setId(existing, COMPANY_ID);
        var command = DashboardCommandFixtures.updateCompanyCommand(COMPANY_ID);
        when(companyRepository.existsById(COMPANY_ID)).thenReturn(true);
        when(companyRepository.findById(COMPANY_ID)).thenReturn(Optional.of(existing));
        when(companyRepository.save(existing)).thenReturn(existing);

        // Act
        Optional<Company> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(DashboardCommandFixtures.VALID_COMPANY_NAME);
        verify(companyRepository, times(1)).existsById(COMPANY_ID);
        verify(companyRepository, times(1)).findById(COMPANY_ID);
        verify(companyRepository, times(1)).save(existing);
        verifyNoMoreInteractions(companyRepository);
    }

    @Test
    @DisplayName("handle(UpdateCompanyCommand) -> throws RuntimeException when id does not exist (AAA)")
    void handleUpdateMissing() {
        // Arrange
        var command = DashboardCommandFixtures.updateCompanyCommand(COMPANY_ID);
        when(companyRepository.existsById(COMPANY_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(COMPANY_ID)).contains("does not exist");
        verify(companyRepository, times(1)).existsById(COMPANY_ID);
        verifyNoMoreInteractions(companyRepository);
    }

    @Test
    @DisplayName("handle(UpdateCompanyCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleUpdateSaveFailure() {
        // Arrange
        var existing = new Company(DashboardCommandFixtures.validCreateCompanyCommand());
        ReflectionTestUtils.setId(existing, COMPANY_ID);
        var command = DashboardCommandFixtures.updateCompanyCommand(COMPANY_ID);
        when(companyRepository.existsById(COMPANY_ID)).thenReturn(true);
        when(companyRepository.findById(COMPANY_ID)).thenReturn(Optional.of(existing));
        when(companyRepository.save(existing)).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error updating Company").contains("boom");
        verify(companyRepository, times(1)).existsById(COMPANY_ID);
        verify(companyRepository, times(1)).findById(COMPANY_ID);
        verify(companyRepository, times(1)).save(existing);
        verifyNoMoreInteractions(companyRepository);
    }

    @Test
    @DisplayName("handle(DeleteCompanyCommand) -> deletes when present (AAA)")
    void handleDeleteSuccess() {
        // Arrange
        var command = new DeleteCompanyCommand(COMPANY_ID);
        when(companyRepository.existsById(COMPANY_ID)).thenReturn(true);

        // Act
        service.handle(command);

        // Assert
        verify(companyRepository, times(1)).existsById(COMPANY_ID);
        verify(companyRepository, times(1)).deleteById(COMPANY_ID);
        verifyNoMoreInteractions(companyRepository);
    }

    @Test
    @DisplayName("handle(DeleteCompanyCommand) -> throws RuntimeException when id is absent (AAA)")
    void handleDeleteMissing() {
        // Arrange
        var command = new DeleteCompanyCommand(COMPANY_ID);
        when(companyRepository.existsById(COMPANY_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(COMPANY_ID)).contains("does not exist");
        verify(companyRepository, times(1)).existsById(COMPANY_ID);
        verify(companyRepository, never()).deleteById(any(Long.class));
        verifyNoMoreInteractions(companyRepository);
    }

    @Test
    @DisplayName("handle(DeleteCompanyCommand) -> wraps deleteById failure in RuntimeException (AAA)")
    void handleDeleteDeleteFailure() {
        // Arrange
        var command = new DeleteCompanyCommand(COMPANY_ID);
        when(companyRepository.existsById(COMPANY_ID)).thenReturn(true);
        doThrow(new RuntimeException("fk")).when(companyRepository).deleteById(COMPANY_ID);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error deleting Company").contains("fk");
        verify(companyRepository, times(1)).existsById(COMPANY_ID);
        verify(companyRepository, times(1)).deleteById(COMPANY_ID);
        verifyNoMoreInteractions(companyRepository);
    }
}
