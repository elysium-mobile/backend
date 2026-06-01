package pe.edu.upc.soft.work.platform.dashboard.interfaces.acl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates.Company;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.WorkTeam;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetCompanyByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetWorkTeamByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.CompanyCommandService;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.CompanyQueryService;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.WorkTeamQueryService;
import pe.edu.upc.soft.work.platform.dashboard.test.fixtures.DashboardCommandFixtures;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardContextFacadeTest {

    @Mock
    private CompanyQueryService companyQueryService;
    @Mock
    private CompanyCommandService companyCommandService;
    @Mock
    private WorkTeamQueryService workTeamQueryService;

    @InjectMocks
    private DashboardContextFacade facade;

    @Test
    @DisplayName("existsCompanyById(Long) -> returns true when query service returns Optional with value (AAA)")
    void existsCompanyByIdPresent() {
        // Arrange
        var company = new Company(DashboardCommandFixtures.validCreateCompanyCommand());
        when(companyQueryService.handle(any(GetCompanyByIdQuery.class))).thenReturn(Optional.of(company));

        // Act
        boolean result = facade.existsCompanyById(5L);

        // Assert
        assertThat(result).isTrue();
        verify(companyQueryService, times(1)).handle(any(GetCompanyByIdQuery.class));
        verifyNoMoreInteractions(companyQueryService);
        verifyNoInteractions(companyCommandService, workTeamQueryService);
    }

    @Test
    @DisplayName("existsCompanyById(Long) -> returns false when query service returns Optional.empty (AAA)")
    void existsCompanyByIdAbsent() {
        // Arrange
        when(companyQueryService.handle(any(GetCompanyByIdQuery.class))).thenReturn(Optional.empty());

        // Act
        boolean result = facade.existsCompanyById(5L);

        // Assert
        assertThat(result).isFalse();
        verify(companyQueryService, times(1)).handle(any(GetCompanyByIdQuery.class));
        verifyNoMoreInteractions(companyQueryService);
        verifyNoInteractions(companyCommandService, workTeamQueryService);
    }

    @Test
    @DisplayName("createCompany(...) -> returns id from command service when not null (AAA)")
    void createCompanyReturnsId() {
        // Arrange
        when(companyCommandService.handle(any(CreateCompanyCommand.class))).thenReturn(99L);

        // Act
        Long result = facade.createCompany(
                DashboardCommandFixtures.VALID_COMPANY_NAME,
                DashboardCommandFixtures.VALID_RUC,
                DashboardCommandFixtures.VALID_CONTACT_EMAIL,
                DashboardCommandFixtures.VALID_CONTACT_PHONE);

        // Assert
        assertThat(result).isEqualTo(99L);
        verify(companyCommandService, times(1)).handle(any(CreateCompanyCommand.class));
        verifyNoMoreInteractions(companyCommandService);
        verifyNoInteractions(companyQueryService, workTeamQueryService);
    }

    @Test
    @DisplayName("createCompany(...) -> returns 0L when command service returns null (AAA)")
    void createCompanyReturnsZeroOnNull() {
        // Arrange
        when(companyCommandService.handle(any(CreateCompanyCommand.class))).thenReturn(null);

        // Act
        Long result = facade.createCompany(
                DashboardCommandFixtures.VALID_COMPANY_NAME,
                DashboardCommandFixtures.VALID_RUC,
                DashboardCommandFixtures.VALID_CONTACT_EMAIL,
                DashboardCommandFixtures.VALID_CONTACT_PHONE);

        // Assert
        assertThat(result).isEqualTo(0L);
        verify(companyCommandService, times(1)).handle(any(CreateCompanyCommand.class));
        verifyNoMoreInteractions(companyCommandService);
        verifyNoInteractions(companyQueryService, workTeamQueryService);
    }

    @Test
    @DisplayName("existsWorkTeamById(Long) -> returns true when query service returns Optional with value (AAA)")
    void existsWorkTeamByIdPresent() {
        // Arrange
        var workTeam = new WorkTeam(DashboardCommandFixtures.validCreateWorkTeamCommand());
        when(workTeamQueryService.handle(any(GetWorkTeamByIdQuery.class))).thenReturn(Optional.of(workTeam));

        // Act
        boolean result = facade.existsWorkTeamById(7L);

        // Assert
        assertThat(result).isTrue();
        verify(workTeamQueryService, times(1)).handle(any(GetWorkTeamByIdQuery.class));
        verifyNoMoreInteractions(workTeamQueryService);
        verifyNoInteractions(companyQueryService, companyCommandService);
    }

    @Test
    @DisplayName("existsWorkTeamById(Long) -> returns false when query service returns Optional.empty (AAA)")
    void existsWorkTeamByIdAbsent() {
        // Arrange
        when(workTeamQueryService.handle(any(GetWorkTeamByIdQuery.class))).thenReturn(Optional.empty());

        // Act
        boolean result = facade.existsWorkTeamById(7L);

        // Assert
        assertThat(result).isFalse();
        verify(workTeamQueryService, times(1)).handle(any(GetWorkTeamByIdQuery.class));
        verifyNoMoreInteractions(workTeamQueryService);
        verifyNoInteractions(companyQueryService, companyCommandService);
    }
}
