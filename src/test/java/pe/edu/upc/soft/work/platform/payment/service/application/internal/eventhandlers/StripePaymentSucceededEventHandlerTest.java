package pe.edu.upc.soft.work.platform.payment.service.application.internal.eventhandlers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates.Membership;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates.Payment;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.Order;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.events.MembershipActivatedEvent;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.events.PaymentRegisteredEvent;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.events.StripePaymentSucceededEvent;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects.MembershipStatus;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.MembershipRepository;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.OrderRepository;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.PaymentRepository;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StripePaymentSucceededEventHandlerTest {

    private static final String TRANSACTION_ID = "pi_test_123";
    private static final Long ORDER_ID = 10L;
    private static final Long MEMBERSHIP_ID = 20L;

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MembershipRepository membershipRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private StripePaymentSucceededEventHandler handler;

    @Test
    @DisplayName("on(StripePaymentSucceededEvent) -> skips processing when transactionId was already recorded (idempotency)")
    void skipsWhenTransactionIdAlreadyExists() {
        // Arrange
        var event = new StripePaymentSucceededEvent(this, TRANSACTION_ID, ORDER_ID, 5000L);
        when(paymentRepository.existsByTransactionId(TRANSACTION_ID)).thenReturn(true);

        // Act
        handler.on(event);

        // Assert: nothing else should happen — no duplicate Payment, no re-activation, no events
        verify(paymentRepository, times(1)).existsByTransactionId(TRANSACTION_ID);
        verifyNoMoreInteractions(paymentRepository);
        verify(orderRepository, never()).findById(any());
        verify(membershipRepository, never()).findById(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("on(StripePaymentSucceededEvent) -> creates Payment and activates Membership on first delivery (AAA)")
    void createsPaymentAndActivatesMembershipWhenNew() {
        // Arrange
        var event = new StripePaymentSucceededEvent(this, TRANSACTION_ID, ORDER_ID, 5000L);

        var order = new Order();
        ReflectionTestUtils.setId(order, ORDER_ID);
        order.setMembershipId(MEMBERSHIP_ID);

        var membership = new Membership();
        ReflectionTestUtils.setId(membership, MEMBERSHIP_ID);

        when(paymentRepository.existsByTransactionId(TRANSACTION_ID)).thenReturn(false);
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(inv -> {
            Payment p = inv.getArgument(0);
            ReflectionTestUtils.setId(p, 1L);
            return p;
        });
        when(membershipRepository.findById(MEMBERSHIP_ID)).thenReturn(Optional.of(membership));
        when(membershipRepository.save(membership)).thenReturn(membership);

        // Act
        handler.on(event);

        // Assert
        verify(paymentRepository, times(1)).existsByTransactionId(TRANSACTION_ID);
        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(membershipRepository, times(1)).save(membership);
        assertThat(membership.getMembershipStatus()).isEqualTo(MembershipStatus.ACTIVE);
        verify(eventPublisher, times(1)).publishEvent(any(PaymentRegisteredEvent.class));
        verify(eventPublisher, times(1)).publishEvent(any(MembershipActivatedEvent.class));
    }

    @Test
    @DisplayName("on(StripePaymentSucceededEvent) -> throws NotFoundArgumentException when Order is missing (AAA)")
    void throwsWhenOrderMissing() {
        // Arrange
        var event = new StripePaymentSucceededEvent(this, TRANSACTION_ID, ORDER_ID, 5000L);
        when(paymentRepository.existsByTransactionId(TRANSACTION_ID)).thenReturn(false);
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.empty());

        // Act + Assert
        NotFoundArgumentException ex = assertThrows(NotFoundArgumentException.class, () -> handler.on(event));
        assertThat(ex.getMessage()).contains("Order ID: " + ORDER_ID);
        verify(paymentRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }
}
