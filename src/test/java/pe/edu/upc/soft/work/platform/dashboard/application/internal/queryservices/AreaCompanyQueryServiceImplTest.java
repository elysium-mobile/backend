package pe.edu.upc.soft.work.platform.dashboard.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.AreaCompany;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAllAreaCompanyQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAreaCompanyByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.AreaCompanyRepository;
import pe.edu.upc.soft.work.platform.dashboard.test.fixtures.DashboardCommandFixtures;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AreaCompanyQueryServiceImplTest {

    @Mock
    private AreaCompanyRepository areacompanyRepository;

    @InjectMocks
    private AreaCompanyQueryServiceImpl service;

    private static AreaCompany sample() {
        return new AreaCompany(DashboardCommandFixtures.validCreateAreaCompanyCommand());
    }

    @Test
    @DisplayName("handle(GetAllAreaCompanyQuery) -> returns list from repository (AAA)")
    void handleAllReturnsList() {
        // Arrange
        List<AreaCompany> areas = List.of(sample());
        when(areacompanyRepository.findAll()).thenReturn(areas);

        // Act
        List<AreaCompany> result = service.handle(new GetAllAreaCompanyQuery());

        // Assert
        assertThat(result).containsExactlyElementsOf(areas);
        verify(areacompanyRepository, times(1)).findAll();
        verifyNoMoreInteractions(areacompanyRepository);
    }

    @Test
    @DisplayName("handle(GetAllAreaCompanyQuery) -> returns empty list when none exist (AAA)")
    void handleAllReturnsEmpty() {
        // Arrange
        when(areacompanyRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<AreaCompany> result = service.handle(new GetAllAreaCompanyQuery());

        // Assert
        assertThat(result).isEmpty();
        verify(areacompanyRepository, times(1)).findAll();
        verifyNoMoreInteractions(areacompanyRepository);
    }

    @Test
    @DisplayName("handle(GetAreaCompanyByIdQuery) -> returns Optional with AreaCompany when found (AAA)")
    void handleByIdReturnsPresent() {
        // Arrange
        var area = sample();
        when(areacompanyRepository.findById(22L)).thenReturn(Optional.of(area));

        // Act
        Optional<AreaCompany> result = service.handle(new GetAreaCompanyByIdQuery(22L));

        // Assert
        assertThat(result).isPresent().containsSame(area);
        verify(areacompanyRepository, times(1)).findById(22L);
        verifyNoMoreInteractions(areacompanyRepository);
    }

    @Test
    @DisplayName("handle(GetAreaCompanyByIdQuery) -> returns Optional.empty when no AreaCompany found (AAA)")
    void handleByIdReturnsEmpty() {
        // Arrange
        when(areacompanyRepository.findById(22L)).thenReturn(Optional.empty());

        // Act
        Optional<AreaCompany> result = service.handle(new GetAreaCompanyByIdQuery(22L));

        // Assert
        assertThat(result).isEmpty();
        verify(areacompanyRepository, times(1)).findById(22L);
        verifyNoMoreInteractions(areacompanyRepository);
    }
}
