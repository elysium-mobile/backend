package pe.edu.upc.soft.work.platform.payment.service.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.MembershipPlan;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetAllMembershipPlanQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetMembershipPlanByIdQuery;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.MembershipPlanRepository;
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
class MembershipPlanQueryServiceImplTest {

    @Mock
    private MembershipPlanRepository membershipplanRepository;

    @InjectMocks
    private MembershipPlanQueryServiceImpl service;

    private static MembershipPlan sample() {
        return new MembershipPlan(PaymentCommandFixtures.validCreateMembershipPlanCommand());
    }

    @Test
    @DisplayName("handle(GetAllMembershipPlanQuery) -> returns list from repository (AAA)")
    void handleAllReturnsList() {
        // Arrange
        List<MembershipPlan> plans = List.of(sample());
        when(membershipplanRepository.findAll()).thenReturn(plans);

        // Act
        List<MembershipPlan> result = service.handle(new GetAllMembershipPlanQuery());

        // Assert
        assertThat(result).containsExactlyElementsOf(plans);
        verify(membershipplanRepository, times(1)).findAll();
        verifyNoMoreInteractions(membershipplanRepository);
    }

    @Test
    @DisplayName("handle(GetAllMembershipPlanQuery) -> returns empty list when none exist (AAA)")
    void handleAllReturnsEmpty() {
        // Arrange
        when(membershipplanRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<MembershipPlan> result = service.handle(new GetAllMembershipPlanQuery());

        // Assert
        assertThat(result).isEmpty();
        verify(membershipplanRepository, times(1)).findAll();
        verifyNoMoreInteractions(membershipplanRepository);
    }

    @Test
    @DisplayName("handle(GetMembershipPlanByIdQuery) -> returns Optional with MembershipPlan when found (AAA)")
    void handleByIdReturnsPresent() {
        // Arrange
        var plan = sample();
        when(membershipplanRepository.findById(31L)).thenReturn(Optional.of(plan));

        // Act
        Optional<MembershipPlan> result = service.handle(new GetMembershipPlanByIdQuery(31L));

        // Assert
        assertThat(result).isPresent().containsSame(plan);
        verify(membershipplanRepository, times(1)).findById(31L);
        verifyNoMoreInteractions(membershipplanRepository);
    }

    @Test
    @DisplayName("handle(GetMembershipPlanByIdQuery) -> returns Optional.empty when no MembershipPlan found (AAA)")
    void handleByIdReturnsEmpty() {
        // Arrange
        when(membershipplanRepository.findById(31L)).thenReturn(Optional.empty());

        // Act
        Optional<MembershipPlan> result = service.handle(new GetMembershipPlanByIdQuery(31L));

        // Assert
        assertThat(result).isEmpty();
        verify(membershipplanRepository, times(1)).findById(31L);
        verifyNoMoreInteractions(membershipplanRepository);
    }
}
