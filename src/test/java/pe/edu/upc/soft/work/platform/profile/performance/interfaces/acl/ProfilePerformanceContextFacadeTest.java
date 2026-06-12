package pe.edu.upc.soft.work.platform.profile.performance.interfaces.acl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.aggregates.Performance;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.queries.GetPerformanceByIdQuery;
import pe.edu.upc.soft.work.platform.profile.performance.domain.services.PerformanceQueryService;
import pe.edu.upc.soft.work.platform.profile.performance.test.fixtures.ProfilePerformanceCommandFixtures;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfilePerformanceContextFacadeTest {

    @Mock
    private PerformanceQueryService performanceQueryService;

    @InjectMocks
    private ProfilePerformanceContextFacade facade;

    @Test
    @DisplayName("existsPerformanceById(Long) -> returns true when query service returns Optional with value (AAA)")
    void existsPerformanceByIdPresent() {
        // Arrange
        var performance = new Performance(ProfilePerformanceCommandFixtures.validCreatePerformanceCommand());
        when(performanceQueryService.handle(any(GetPerformanceByIdQuery.class))).thenReturn(Optional.of(performance));

        // Act
        boolean result = facade.existsPerformanceById(23L);

        // Assert
        assertThat(result).isTrue();
        verify(performanceQueryService, times(1)).handle(any(GetPerformanceByIdQuery.class));
        verifyNoMoreInteractions(performanceQueryService);
    }

    @Test
    @DisplayName("existsPerformanceById(Long) -> returns false when query service returns Optional.empty (AAA)")
    void existsPerformanceByIdAbsent() {
        // Arrange
        when(performanceQueryService.handle(any(GetPerformanceByIdQuery.class))).thenReturn(Optional.empty());

        // Act
        boolean result = facade.existsPerformanceById(23L);

        // Assert
        assertThat(result).isFalse();
        verify(performanceQueryService, times(1)).handle(any(GetPerformanceByIdQuery.class));
        verifyNoMoreInteractions(performanceQueryService);
    }
}
