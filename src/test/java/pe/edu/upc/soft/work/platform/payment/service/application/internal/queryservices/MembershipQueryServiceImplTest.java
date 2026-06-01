package pe.edu.upc.soft.work.platform.payment.service.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates.Membership;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetAllMembershipQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetMembershipByIdQuery;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.MembershipRepository;
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
class MembershipQueryServiceImplTest {

    @Mock
    private MembershipRepository membershipRepository;

    @InjectMocks
    private MembershipQueryServiceImpl service;

    private static Membership sample() {
        return new Membership(PaymentCommandFixtures.validCreateMembershipCommand());
    }

    @Test
    @DisplayName("handle(GetAllMembershipQuery) -> returns list from repository (AAA)")
    void handleAllReturnsList() {
        // Arrange
        List<Membership> memberships = List.of(sample());
        when(membershipRepository.findAll()).thenReturn(memberships);

        // Act
        List<Membership> result = service.handle(new GetAllMembershipQuery());

        // Assert
        assertThat(result).containsExactlyElementsOf(memberships);
        verify(membershipRepository, times(1)).findAll();
        verifyNoMoreInteractions(membershipRepository);
    }

    @Test
    @DisplayName("handle(GetAllMembershipQuery) -> returns empty list when none exist (AAA)")
    void handleAllReturnsEmpty() {
        // Arrange
        when(membershipRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Membership> result = service.handle(new GetAllMembershipQuery());

        // Assert
        assertThat(result).isEmpty();
        verify(membershipRepository, times(1)).findAll();
        verifyNoMoreInteractions(membershipRepository);
    }

    @Test
    @DisplayName("handle(GetMembershipByIdQuery) -> returns Optional with Membership when found (AAA)")
    void handleByIdReturnsPresent() {
        // Arrange
        var membership = sample();
        when(membershipRepository.findById(5L)).thenReturn(Optional.of(membership));

        // Act
        Optional<Membership> result = service.handle(new GetMembershipByIdQuery(5L));

        // Assert
        assertThat(result).isPresent().containsSame(membership);
        verify(membershipRepository, times(1)).findById(5L);
        verifyNoMoreInteractions(membershipRepository);
    }

    @Test
    @DisplayName("handle(GetMembershipByIdQuery) -> returns Optional.empty when no Membership found (AAA)")
    void handleByIdReturnsEmpty() {
        // Arrange
        when(membershipRepository.findById(5L)).thenReturn(Optional.empty());

        // Act
        Optional<Membership> result = service.handle(new GetMembershipByIdQuery(5L));

        // Assert
        assertThat(result).isEmpty();
        verify(membershipRepository, times(1)).findById(5L);
        verifyNoMoreInteractions(membershipRepository);
    }
}
