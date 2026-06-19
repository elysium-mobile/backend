package pe.edu.upc.soft.work.platform.dashboard.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.dashboard.application.internal.outboundservices.acl.ExternalIamServiceFromDashboard;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates.Company;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.DeleteAreaCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.AreaCompany;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.AreaCompanyRepository;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.CompanyRepository;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.UnitOfWorkRepository;
import pe.edu.upc.soft.work.platform.dashboard.test.fixtures.DashboardCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AreaCompanyCommandServiceImplTest {

    private static final Long AREA_COMPANY_ID = 22L;

    @Mock
    private AreaCompanyRepository areacompanyRepository;

    @Mock
    private ExternalIamServiceFromDashboard externalIamServiceFromDashboard;

    @InjectMocks
    private AreaCompanyCommandServiceImpl service;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private UnitOfWorkRepository unitOfWorkRepository;

    @Test
    @DisplayName("handle(CreateAreaCompanyCommand) -> creates AreaCompany and returns generated id (AAA)")
    void handleCreateSuccess() {
        // Arrange
        var command = DashboardCommandFixtures.validCreateAreaCompanyCommand();
        when(companyRepository.existsById(command.companyId())).thenReturn(true);
        when(areacompanyRepository.save(any(AreaCompany.class))).thenAnswer(inv -> {
            AreaCompany a = inv.getArgument(0);
            ReflectionTestUtils.setId(a, AREA_COMPANY_ID);
            return a;
        });


        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(AREA_COMPANY_ID);
        verify(areacompanyRepository, times(1)).save(any(AreaCompany.class));
        verifyNoMoreInteractions(areacompanyRepository);
        verify(companyRepository).existsById(command.companyId());
        verifyNoInteractions(externalIamServiceFromDashboard);
    }

    @Test
    @DisplayName("handle(CreateAreaCompanyCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleCreateSaveFailure() {
        // Arrange
        var command = DashboardCommandFixtures.validCreateAreaCompanyCommand();
        when(companyRepository.existsById(command.companyId())).thenReturn(true);
        when(areacompanyRepository.save(any(AreaCompany.class))).thenThrow(new RuntimeException("db"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error creating AreaCompany").contains("db");
        verify(companyRepository, times(1)).existsById(command.companyId());
        verify(areacompanyRepository, times(1)).save(any(AreaCompany.class));
        verifyNoMoreInteractions(areacompanyRepository);
        verifyNoInteractions(externalIamServiceFromDashboard);
    }

    @Test
    @DisplayName("handle(UpdateAreaCompanyCommand) -> returns Optional with updated AreaCompany when present (AAA)")
    void handleUpdateSuccess() {
        // Arrange
        var existing = new AreaCompany(DashboardCommandFixtures.validCreateAreaCompanyCommand());
        ReflectionTestUtils.setId(existing, AREA_COMPANY_ID);
        var command = DashboardCommandFixtures.updateAreaCompanyCommand(AREA_COMPANY_ID);
        when(companyRepository.existsById(command.companyId())).thenReturn(true);
        when(areacompanyRepository.existsById(AREA_COMPANY_ID)).thenReturn(true);
        when(areacompanyRepository.findById(AREA_COMPANY_ID)).thenReturn(Optional.of(existing));
        when(areacompanyRepository.save(existing)).thenReturn(existing);

        // Act
        Optional<AreaCompany> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(DashboardCommandFixtures.VALID_AREA_NAME);
        verify(companyRepository, times(1)).existsById(command.companyId());
        verify(areacompanyRepository, times(1)).existsById(AREA_COMPANY_ID);
        verify(areacompanyRepository, times(1)).findById(AREA_COMPANY_ID);
        verify(areacompanyRepository, times(1)).save(existing);
        verifyNoMoreInteractions(areacompanyRepository);
        verifyNoInteractions(externalIamServiceFromDashboard);
    }

    @Test
    @DisplayName("handle(UpdateAreaCompanyCommand) -> throws RuntimeException when id does not exist (AAA)")
    void handleUpdateMissing() {
        // Arrange
        var command = DashboardCommandFixtures.updateAreaCompanyCommand(AREA_COMPANY_ID);
        when(areacompanyRepository.existsById(AREA_COMPANY_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(AREA_COMPANY_ID)).contains("does not exist");
        verify(areacompanyRepository, times(1)).existsById(AREA_COMPANY_ID);
        verifyNoMoreInteractions(areacompanyRepository);
        verifyNoInteractions(externalIamServiceFromDashboard);
    }

    @Test
    @DisplayName("handle(UpdateAreaCompanyCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleUpdateSaveFailure() {
        // Arrange
        var existing = new AreaCompany(DashboardCommandFixtures.validCreateAreaCompanyCommand());
        ReflectionTestUtils.setId(existing, AREA_COMPANY_ID);
        var command = DashboardCommandFixtures.updateAreaCompanyCommand(AREA_COMPANY_ID);
        when(companyRepository.existsById(command.companyId())).thenReturn(true);
        when(areacompanyRepository.existsById(AREA_COMPANY_ID)).thenReturn(true);
        when(areacompanyRepository.findById(AREA_COMPANY_ID)).thenReturn(Optional.of(existing));
        when(areacompanyRepository.save(existing)).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error updating AreaCompany").contains("boom");
        verify(areacompanyRepository, times(1)).existsById(AREA_COMPANY_ID);
        verify(areacompanyRepository, times(1)).findById(AREA_COMPANY_ID);
        verify(areacompanyRepository, times(1)).save(existing);
        verifyNoMoreInteractions(areacompanyRepository);
        verifyNoInteractions(externalIamServiceFromDashboard);
    }

    @Test
    @DisplayName("handle(DeleteAreaCompanyCommand) -> deletes when present (AAA)")
    void handleDeleteSuccess() {
        // Arrange
        var command = new DeleteAreaCompanyCommand(AREA_COMPANY_ID);
        var areaCompany = new AreaCompany();
        ReflectionTestUtils.setId(areaCompany, AREA_COMPANY_ID);

        var company = new Company();
        company.addAreaCompany(areaCompany);

        when(areacompanyRepository.findById(AREA_COMPANY_ID)).thenReturn(Optional.of(areaCompany));
        when(companyRepository.findById(areaCompany.getCompanyId())).thenReturn(Optional.of(company));

        // Act
        service.handle(command);

        // Assert
        verify(companyRepository, times(1)).save(any(Company.class));
        verifyNoMoreInteractions(areacompanyRepository);
        verifyNoInteractions(externalIamServiceFromDashboard);
    }

    @Test
    @DisplayName("handle(DeleteAreaCompanyCommand) -> throws RuntimeException when id is absent (AAA)")
    void handleDeleteMissing() {
        // Arrange
        var command = new DeleteAreaCompanyCommand(AREA_COMPANY_ID);
        when(areacompanyRepository.findById(AREA_COMPANY_ID)).thenReturn(Optional.empty());

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(AREA_COMPANY_ID)).contains("does not exist");

        // Assert
        verify(areacompanyRepository, times(1)).findById(AREA_COMPANY_ID);
        verifyNoInteractions(companyRepository);
        verifyNoInteractions(externalIamServiceFromDashboard);
    }

    @Test
    @DisplayName("handle(DeleteAreaCompanyCommand) -> wraps deleteById failure in RuntimeException (AAA)")
    void handleDeleteDeleteFailure() {
        // Arrange
        var command = new DeleteAreaCompanyCommand(AREA_COMPANY_ID);
        var areaCompany = new AreaCompany();
        ReflectionTestUtils.setId(areaCompany, AREA_COMPANY_ID);
        areaCompany.setCompanyId(1L);

        var company = new Company();
        company.addAreaCompany(areaCompany);

        when(areacompanyRepository.findById(AREA_COMPANY_ID)).thenReturn(Optional.of(areaCompany));
        when(companyRepository.findById(areaCompany.getCompanyId())).thenReturn(Optional.of(company));
        when(companyRepository.save(any(Company.class))).thenThrow(new RuntimeException("fk"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error deleting AreaCompany").contains("fk");
        verify(companyRepository, times(1)).save(any(Company.class));
        verifyNoInteractions(externalIamServiceFromDashboard);
    }
}
