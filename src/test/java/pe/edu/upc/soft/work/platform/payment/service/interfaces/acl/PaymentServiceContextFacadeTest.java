package pe.edu.upc.soft.work.platform.payment.service.interfaces.acl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates.Membership;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates.Payment;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreatePaymentCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetMembershipByIdQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetPaymentByIdQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.MembershipQueryService;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.PaymentCommandService;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.PaymentQueryService;
import pe.edu.upc.soft.work.platform.payment.service.test.fixtures.PaymentCommandFixtures;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceContextFacadeTest {

    @Mock
    private PaymentCommandService paymentCommandService;
    @Mock
    private PaymentQueryService paymentQueryService;
    @Mock
    private MembershipQueryService membershipQueryService;

    @InjectMocks
    private PaymentServiceContextFacade facade;

    @Test
    @DisplayName("existsPaymentById(Long) -> returns true when query service returns Optional with value (AAA)")
    void existsPaymentByIdPresent() {
        // Arrange
        var payment = new Payment(PaymentCommandFixtures.validCreatePaymentCommand());
        when(paymentQueryService.handle(any(GetPaymentByIdQuery.class))).thenReturn(Optional.of(payment));

        // Act
        boolean result = facade.existsPaymentById(51L);

        // Assert
        assertThat(result).isTrue();
        verify(paymentQueryService, times(1)).handle(any(GetPaymentByIdQuery.class));
        verifyNoMoreInteractions(paymentQueryService);
        verifyNoInteractions(paymentCommandService, membershipQueryService);
    }

    @Test
    @DisplayName("existsPaymentById(Long) -> returns false when query service returns Optional.empty (AAA)")
    void existsPaymentByIdAbsent() {
        // Arrange
        when(paymentQueryService.handle(any(GetPaymentByIdQuery.class))).thenReturn(Optional.empty());

        // Act
        boolean result = facade.existsPaymentById(51L);

        // Assert
        assertThat(result).isFalse();
        verify(paymentQueryService, times(1)).handle(any(GetPaymentByIdQuery.class));
        verifyNoMoreInteractions(paymentQueryService);
        verifyNoInteractions(paymentCommandService, membershipQueryService);
    }

    @Test
    @DisplayName("existsMembershipById(Long) -> returns true when query service returns Optional with value (AAA)")
    void existsMembershipByIdPresent() {
        // Arrange
        var membership = new Membership(PaymentCommandFixtures.validCreateMembershipCommand());
        when(membershipQueryService.handle(any(GetMembershipByIdQuery.class))).thenReturn(Optional.of(membership));

        // Act
        boolean result = facade.existsMembershipById(5L);

        // Assert
        assertThat(result).isTrue();
        verify(membershipQueryService, times(1)).handle(any(GetMembershipByIdQuery.class));
        verifyNoMoreInteractions(membershipQueryService);
        verifyNoInteractions(paymentCommandService, paymentQueryService);
    }

    @Test
    @DisplayName("existsMembershipById(Long) -> returns false when query service returns Optional.empty (AAA)")
    void existsMembershipByIdAbsent() {
        // Arrange
        when(membershipQueryService.handle(any(GetMembershipByIdQuery.class))).thenReturn(Optional.empty());

        // Act
        boolean result = facade.existsMembershipById(5L);

        // Assert
        assertThat(result).isFalse();
        verify(membershipQueryService, times(1)).handle(any(GetMembershipByIdQuery.class));
        verifyNoMoreInteractions(membershipQueryService);
        verifyNoInteractions(paymentCommandService, paymentQueryService);
    }

    @Test
    @DisplayName("createPayment(...) -> returns id from command service when not null (AAA)")
    void createPaymentReturnsId() {
        // Arrange
        when(paymentCommandService.handle(any(CreatePaymentCommand.class))).thenReturn(77L);

        // Act
        Long result = facade.createPayment(
                PaymentCommandFixtures.VALID_ORDER_ID,
                PaymentCommandFixtures.VALID_TRANSACTION_ID,
                PaymentCommandFixtures.VALID_PAYMENT_DATE,
                PaymentCommandFixtures.VALID_PAYMENT_STATUS,
            PaymentCommandFixtures.VALID_METHOD
        );

        // Assert
        assertThat(result).isEqualTo(77L);
        verify(paymentCommandService, times(1)).handle(any(CreatePaymentCommand.class));
        verifyNoMoreInteractions(paymentCommandService);
        verifyNoInteractions(paymentQueryService, membershipQueryService);
    }

    @Test
    @DisplayName("createPayment(...) -> returns 0L when command service returns null (AAA)")
    void createPaymentReturnsZeroOnNull() {
        // Arrange
        when(paymentCommandService.handle(any(CreatePaymentCommand.class))).thenReturn(null);

        // Act
        Long result = facade.createPayment(
                PaymentCommandFixtures.VALID_ORDER_ID,
                PaymentCommandFixtures.VALID_TRANSACTION_ID,
                PaymentCommandFixtures.VALID_PAYMENT_DATE,
            PaymentCommandFixtures.VALID_PAYMENT_STATUS,
            PaymentCommandFixtures.VALID_METHOD
            );

        // Assert
        assertThat(result).isEqualTo(0L);
        verify(paymentCommandService, times(1)).handle(any(CreatePaymentCommand.class));
        verifyNoMoreInteractions(paymentCommandService);
        verifyNoInteractions(paymentQueryService, membershipQueryService);
    }
}
