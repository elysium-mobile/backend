package pe.edu.upc.soft.work.platform.worker.forum.application.internal.outboundservices.acl;

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
class ExternalIamServiceFromWorkerForumTest {

    @Mock
    private IamContextFacade iamContextFacade;

    @InjectMocks
    private ExternalIamServiceFromWorkerForum service;

    @Test
    @DisplayName("existsUserAccountById(Long) -> returns true when facade confirms existence (AAA)")
    void existsUserAccountByIdReturnsTrue() {
        // Arrange
        when(iamContextFacade.existsUserAccountById(10L)).thenReturn(true);

        // Act
        boolean result = service.existsUserAccountById(10L);

        // Assert
        assertThat(result).isTrue();
        verify(iamContextFacade, times(1)).existsUserAccountById(10L);
        verifyNoMoreInteractions(iamContextFacade);
    }

    @Test
    @DisplayName("existsUserAccountById(Long) -> returns false when facade reports absence (AAA)")
    void existsUserAccountByIdReturnsFalse() {
        // Arrange
        when(iamContextFacade.existsUserAccountById(10L)).thenReturn(false);

        // Act
        boolean result = service.existsUserAccountById(10L);

        // Assert
        assertThat(result).isFalse();
        verify(iamContextFacade, times(1)).existsUserAccountById(10L);
        verifyNoMoreInteractions(iamContextFacade);
    }
}
