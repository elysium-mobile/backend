package pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.acl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.acl.DashboardContextFacade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalDashboardServiceFromIAMTest {

    @Mock
    private DashboardContextFacade dashboardContextFacade;

    @InjectMocks
    private ExternalDashboardServiceFromIAM service;

    @Test
    @DisplayName("existsCompanyById(Long) -> returns true when facade confirms existence (AAA)")
    void existsCompanyByIdReturnsTrue() {
        // Arrange
        when(dashboardContextFacade.existsCompanyById(5L)).thenReturn(true);

        // Act
        boolean result = service.existsCompanyById(5L);

        // Assert
        assertThat(result).isTrue();
        verify(dashboardContextFacade).existsCompanyById(5L);
        verifyNoMoreInteractions(dashboardContextFacade);
    }

    @Test
    @DisplayName("existsCompanyById(Long) -> returns false when facade reports absence (AAA)")
    void existsCompanyByIdReturnsFalse() {
        // Arrange
        when(dashboardContextFacade.existsCompanyById(5L)).thenReturn(false);

        // Act
        boolean result = service.existsCompanyById(5L);

        // Assert
        assertThat(result).isFalse();
        verify(dashboardContextFacade).existsCompanyById(5L);
        verifyNoMoreInteractions(dashboardContextFacade);
    }

    @Test
    @DisplayName("existsWorkTeamById(Long) -> returns true when facade confirms existence (AAA)")
    void existsWorkTeamByIdReturnsTrue() {
        // Arrange
        when(dashboardContextFacade.existsWorkTeamById(8L)).thenReturn(true);

        // Act
        boolean result = service.existsWorkTeamById(8L);

        // Assert
        assertThat(result).isTrue();
        verify(dashboardContextFacade).existsWorkTeamById(8L);
        verifyNoMoreInteractions(dashboardContextFacade);
    }

    @Test
    @DisplayName("existsWorkTeamById(Long) -> returns false when facade reports absence (AAA)")
    void existsWorkTeamByIdReturnsFalse() {
        // Arrange
        when(dashboardContextFacade.existsWorkTeamById(8L)).thenReturn(false);

        // Act
        boolean result = service.existsWorkTeamById(8L);

        // Assert
        assertThat(result).isFalse();
        verify(dashboardContextFacade).existsWorkTeamById(8L);
        verifyNoMoreInteractions(dashboardContextFacade);
    }
}
