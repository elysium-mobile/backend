package pe.edu.upc.soft.work.platform.dashboard.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates.Company;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAllCompanyQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetCompanyByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.CompanyRepository;
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
class CompanyQueryServiceImplTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyQueryServiceImpl service;

    private static Company sample() {
        return new Company(DashboardCommandFixtures.validCreateCompanyCommand());
    }

    @Test
    @DisplayName("handle(GetAllCompanyQuery) -> returns list from repository (AAA)")
    void handleAllReturnsList() {
        // Arrange
        List<Company> companies = List.of(sample(), sample());
        when(companyRepository.findAll()).thenReturn(companies);

        // Act
        List<Company> result = service.handle(new GetAllCompanyQuery());

        // Assert
        assertThat(result).hasSize(2).containsExactlyElementsOf(companies);
        verify(companyRepository, times(1)).findAll();
        verifyNoMoreInteractions(companyRepository);
    }

    @Test
    @DisplayName("handle(GetAllCompanyQuery) -> returns empty list when none exist (AAA)")
    void handleAllReturnsEmpty() {
        // Arrange
        when(companyRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Company> result = service.handle(new GetAllCompanyQuery());

        // Assert
        assertThat(result).isEmpty();
        verify(companyRepository, times(1)).findAll();
        verifyNoMoreInteractions(companyRepository);
    }

    @Test
    @DisplayName("handle(GetCompanyByIdQuery) -> returns Optional with Company when found (AAA)")
    void handleByIdReturnsPresent() {
        // Arrange
        var company = sample();
        when(companyRepository.findById(12L)).thenReturn(Optional.of(company));

        // Act
        Optional<Company> result = service.handle(new GetCompanyByIdQuery(12L));

        // Assert
        assertThat(result).isPresent().containsSame(company);
        verify(companyRepository, times(1)).findById(12L);
        verifyNoMoreInteractions(companyRepository);
    }

    @Test
    @DisplayName("handle(GetCompanyByIdQuery) -> returns Optional.empty when no Company found (AAA)")
    void handleByIdReturnsEmpty() {
        // Arrange
        when(companyRepository.findById(12L)).thenReturn(Optional.empty());

        // Act
        Optional<Company> result = service.handle(new GetCompanyByIdQuery(12L));

        // Assert
        assertThat(result).isEmpty();
        verify(companyRepository, times(1)).findById(12L);
        verifyNoMoreInteractions(companyRepository);
    }
}
