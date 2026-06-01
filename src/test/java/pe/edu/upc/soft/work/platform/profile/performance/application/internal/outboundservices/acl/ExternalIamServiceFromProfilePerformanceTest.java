package pe.edu.upc.soft.work.platform.profile.performance.application.internal.outboundservices.acl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.iam.interfaces.acl.IamContextFacade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalIamServiceFromProfilePerformanceTest {

    @Mock
    private IamContextFacade iamContextFacade;

    @InjectMocks
    private ExternalIamServiceFromProfilePerformance service;

    @Test
    @DisplayName("existsEmployeeProfileById(Long) -> returns true when facade confirms existence (AAA)")
    void existsEmployeeProfileByIdReturnsTrue() {
        // Arrange
        when(iamContextFacade.existsEmployeeProfileById(8L)).thenReturn(true);

        // Act
        boolean result = service.existsEmployeeProfileById(8L);

        // Assert
        assertThat(result).isTrue();
        verify(iamContextFacade, times(1)).existsEmployeeProfileById(8L);
        verifyNoMoreInteractions(iamContextFacade);
    }

    @Test
    @DisplayName("existsEmployeeProfileById(Long) -> returns false when facade reports absence (AAA)")
    void existsEmployeeProfileByIdReturnsFalse() {
        // Arrange
        when(iamContextFacade.existsEmployeeProfileById(8L)).thenReturn(false);

        // Act
        boolean result = service.existsEmployeeProfileById(8L);

        // Assert
        assertThat(result).isFalse();
        verify(iamContextFacade, times(1)).existsEmployeeProfileById(8L);
        verifyNoMoreInteractions(iamContextFacade);
    }

    @Test
    @DisplayName("existsRRHHProfileById(Long) -> returns true when facade confirms existence (AAA)")
    void existsRRHHProfileByIdReturnsTrue() {
        // Arrange
        when(iamContextFacade.existsRRHHProfileById(11L)).thenReturn(true);

        // Act
        boolean result = service.existsRRHHProfileById(11L);

        // Assert
        assertThat(result).isTrue();
        verify(iamContextFacade, times(1)).existsRRHHProfileById(11L);
        verifyNoMoreInteractions(iamContextFacade);
    }

    @Test
    @DisplayName("existsRRHHProfileById(Long) -> returns false when facade reports absence (AAA)")
    void existsRRHHProfileByIdReturnsFalse() {
        // Arrange
        when(iamContextFacade.existsRRHHProfileById(11L)).thenReturn(false);

        // Act
        boolean result = service.existsRRHHProfileById(11L);

        // Assert
        assertThat(result).isFalse();
        verify(iamContextFacade, times(1)).existsRRHHProfileById(11L);
        verifyNoMoreInteractions(iamContextFacade);
    }
}
