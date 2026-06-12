package pe.edu.upc.soft.work.platform.worker.forum.application.internal.outboundservices.acl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.acl.DashboardContextFacade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalDashboardServiceFromWorkerForumTest {

    @Mock
    private DashboardContextFacade dashboardContextFacade;

    @InjectMocks
    private ExternalDashboardServiceFromWorkerForum service;

    @Test
    @DisplayName("existsCompanyById(Long) -> returns true when facade confirms existence (AAA)")
    void existsCompanyByIdReturnsTrue() {
        // Arrange
        when(dashboardContextFacade.existsCompanyById(5L)).thenReturn(true);

        // Act
        boolean result = service.existsCompanyById(5L);

        // Assert
        assertThat(result).isTrue();
        verify(dashboardContextFacade, times(1)).existsCompanyById(5L);
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
        verify(dashboardContextFacade, times(1)).existsCompanyById(5L);
        verifyNoMoreInteractions(dashboardContextFacade);
    }
}
