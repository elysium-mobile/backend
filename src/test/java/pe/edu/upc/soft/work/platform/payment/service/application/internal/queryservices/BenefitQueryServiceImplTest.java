package pe.edu.upc.soft.work.platform.payment.service.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.Benefit;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetAllBenefitQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetBenefitByIdQuery;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.BenefitRepository;
import pe.edu.upc.soft.work.platform.payment.service.test.fixtures.PaymentCommandFixtures;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BenefitQueryServiceImplTest {

    @Mock
    private BenefitRepository benefitRepository;

    @InjectMocks
    private BenefitQueryServiceImpl service;

    private static Benefit sample() {
        return new Benefit(PaymentCommandFixtures.validCreateBenefitCommand());
    }

    @Test
    @DisplayName("handle(GetAllBenefitQuery) -> returns list from repository (AAA)")
    void handleAllReturnsList() {
        // Arrange
        List<Benefit> benefits = List.of(sample(), sample());
        when(benefitRepository.findAll()).thenReturn(benefits);

        // Act
        List<Benefit> result = service.handle(new GetAllBenefitQuery());

        // Assert
        assertThat(result).hasSize(2).containsExactlyElementsOf(benefits);
        verify(benefitRepository, times(1)).findAll();
        verifyNoMoreInteractions(benefitRepository);
    }

    @Test
    @DisplayName("handle(GetAllBenefitQuery) -> returns empty list when none exist (AAA)")
    void handleAllReturnsEmpty() {
        // Arrange
        when(benefitRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Benefit> result = service.handle(new GetAllBenefitQuery());

        // Assert
        assertThat(result).isEmpty();
        verify(benefitRepository, times(1)).findAll();
        verifyNoMoreInteractions(benefitRepository);
    }

    @Test
    @DisplayName("handle(GetBenefitByIdQuery) -> returns Optional with Benefit when found (AAA)")
    void handleByIdReturnsPresent() {
        // Arrange
        var benefit = sample();
        when(benefitRepository.findById(11L)).thenReturn(Optional.of(benefit));

        // Act
        Optional<Benefit> result = service.handle(new GetBenefitByIdQuery(11L));

        // Assert
        assertThat(result).isPresent().containsSame(benefit);
        verify(benefitRepository, times(1)).findById(11L);
        verifyNoMoreInteractions(benefitRepository);
    }

    @Test
    @DisplayName("handle(GetBenefitByIdQuery) -> returns Optional.empty when no Benefit found (AAA)")
    void handleByIdReturnsEmpty() {
        // Arrange
        when(benefitRepository.findById(11L)).thenReturn(Optional.empty());

        // Act
        Optional<Benefit> result = service.handle(new GetBenefitByIdQuery(11L));

        // Assert
        assertThat(result).isEmpty();
        verify(benefitRepository, times(1)).findById(11L);
        verifyNoMoreInteractions(benefitRepository);
    }
}
