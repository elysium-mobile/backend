package pe.edu.upc.soft.work.platform.payment.service.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates.Membership;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates.Payment;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.DeletePaymentCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.Order;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.events.PaymentRegisteredEvent;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.MembershipRepository;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.OrderRepository;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.PaymentRepository;
import pe.edu.upc.soft.work.platform.payment.service.test.fixtures.PaymentCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentCommandServiceImplTest {

    private static final Long PAYMENT_ID = 51L;

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private MembershipRepository membershipRepository;

    @InjectMocks
    private PaymentCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreatePaymentCommand) -> creates Payment when order exists (AAA)")
    void handleCreateSuccess() {
        // Arrange
        var command = PaymentCommandFixtures.validCreatePaymentCommand();
        var order = new Order();
        ReflectionTestUtils.setId(order, command.orderId());
        order.setMembershipId(1L);
        var membership = new Membership();
        ReflectionTestUtils.setId(membership, 1L);
        when(orderRepository.existsById(command.orderId())).thenReturn(true);
        when(orderRepository.findById(command.orderId())).thenReturn(Optional.of(order));
        when(membershipRepository.findById(1L)).thenReturn(Optional.of(membership));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(inv -> {
            Payment p = inv.getArgument(0);
            ReflectionTestUtils.setId(p, PAYMENT_ID);
            return p;
        });

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(PAYMENT_ID);
        verify(eventPublisher, times(2)).publishEvent(any());
        verify(orderRepository, times(1)).existsById(command.orderId());
        verify(orderRepository, times(1)).findById(command.orderId());
        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(membershipRepository, times(1)).findById(1L);
        verify(membershipRepository, times(1)).save(any(Membership.class));
        verifyNoMoreInteractions(orderRepository, paymentRepository, membershipRepository, eventPublisher);
    }

    @Test
    @DisplayName("handle(CreatePaymentCommand) -> throws NotFoundArgumentException when order is missing (AAA)")
    void handleCreateMissingOrder() {
        // Arrange
        var command = PaymentCommandFixtures.validCreatePaymentCommand();
        when(orderRepository.existsById(PaymentCommandFixtures.VALID_ORDER_ID)).thenReturn(false);

        // Act + Assert
        NotFoundArgumentException ex = assertThrows(NotFoundArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Order ID: " + PaymentCommandFixtures.VALID_ORDER_ID);
        verify(orderRepository, times(1)).existsById(PaymentCommandFixtures.VALID_ORDER_ID);
        verifyNoMoreInteractions(orderRepository);
        verifyNoInteractions(paymentRepository);
    }

    @Test
    @DisplayName("handle(CreatePaymentCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleCreateSaveFailure() {
        // Arrange
        var command = PaymentCommandFixtures.validCreatePaymentCommand();
        var order = new Order();
        order.setMembershipId(1L);

        when(orderRepository.existsById(command.orderId())).thenReturn(true);
        when(orderRepository.findById(command.orderId())).thenReturn(Optional.of(order));
        when(paymentRepository.save(any(Payment.class))).thenThrow(new RuntimeException("db"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error creating Payment").contains("db");
        verify(eventPublisher, never()).publishEvent(any(PaymentRegisteredEvent.class));
        verify(orderRepository, times(1)).existsById(command.orderId());
        verify(orderRepository, times(1)).findById(command.orderId());
        verify(paymentRepository, times(1)).save(any(Payment.class));
        verifyNoMoreInteractions(orderRepository, paymentRepository, eventPublisher, membershipRepository);
    }

    @Test
    @DisplayName("handle(UpdatePaymentCommand) -> returns Optional with updated Payment when present (AAA)")
    void handleUpdateSuccess() {
        // Arrange
        var existing = new Payment(PaymentCommandFixtures.validCreatePaymentCommand());
        ReflectionTestUtils.setId(existing, PAYMENT_ID);
        var command = PaymentCommandFixtures.updatePaymentCommand(PAYMENT_ID);
        when(paymentRepository.existsById(PAYMENT_ID)).thenReturn(true);
        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.of(existing));
        when(paymentRepository.save(existing)).thenReturn(existing);

        // Act
        Optional<Payment> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getTransactionId()).isEqualTo(PaymentCommandFixtures.VALID_TRANSACTION_ID);
        verify(paymentRepository, times(1)).existsById(PAYMENT_ID);
        verify(paymentRepository, times(1)).findById(PAYMENT_ID);
        verify(paymentRepository, times(1)).save(existing);
        verifyNoMoreInteractions(paymentRepository);
        verifyNoInteractions(orderRepository);
    }

    @Test
    @DisplayName("handle(UpdatePaymentCommand) -> throws RuntimeException when id does not exist (AAA)")
    void handleUpdateMissing() {
        // Arrange
        var command = PaymentCommandFixtures.updatePaymentCommand(PAYMENT_ID);
        when(paymentRepository.existsById(PAYMENT_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(PAYMENT_ID)).contains("does not exist");
        verify(paymentRepository, times(1)).existsById(PAYMENT_ID);
        verifyNoMoreInteractions(paymentRepository);
        verifyNoInteractions(orderRepository);
    }

    @Test
    @DisplayName("handle(UpdatePaymentCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleUpdateSaveFailure() {
        // Arrange
        var existing = new Payment(PaymentCommandFixtures.validCreatePaymentCommand());
        ReflectionTestUtils.setId(existing, PAYMENT_ID);
        var command = PaymentCommandFixtures.updatePaymentCommand(PAYMENT_ID);
        when(paymentRepository.existsById(PAYMENT_ID)).thenReturn(true);
        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.of(existing));
        when(paymentRepository.save(existing)).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error updating Payment").contains("boom");
        verify(paymentRepository, times(1)).existsById(PAYMENT_ID);
        verify(paymentRepository, times(1)).findById(PAYMENT_ID);
        verify(paymentRepository, times(1)).save(existing);
        verifyNoMoreInteractions(paymentRepository);
        verifyNoInteractions(orderRepository);
    }

    @Test
    @DisplayName("handle(DeletePaymentCommand) -> deletes when present (AAA)")
    void handleDeleteSuccess() {
        // Arrange
        var command = new DeletePaymentCommand(PAYMENT_ID);
        when(paymentRepository.existsById(PAYMENT_ID)).thenReturn(true);

        // Act
        service.handle(command);

        // Assert
        verify(paymentRepository, times(1)).existsById(PAYMENT_ID);
        verify(paymentRepository, times(1)).deleteById(PAYMENT_ID);
        verifyNoMoreInteractions(paymentRepository);
        verifyNoInteractions(orderRepository);
    }

    @Test
    @DisplayName("handle(DeletePaymentCommand) -> throws RuntimeException when id is absent (AAA)")
    void handleDeleteMissing() {
        // Arrange
        var command = new DeletePaymentCommand(PAYMENT_ID);
        when(paymentRepository.existsById(PAYMENT_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(PAYMENT_ID)).contains("does not exist");
        verify(paymentRepository, times(1)).existsById(PAYMENT_ID);
        verify(paymentRepository, never()).deleteById(any(Long.class));
        verifyNoMoreInteractions(paymentRepository);
        verifyNoInteractions(orderRepository);
    }

    @Test
    @DisplayName("handle(DeletePaymentCommand) -> wraps deleteById failure in RuntimeException (AAA)")
    void handleDeleteDeleteFailure() {
        // Arrange
        var command = new DeletePaymentCommand(PAYMENT_ID);
        when(paymentRepository.existsById(PAYMENT_ID)).thenReturn(true);
        doThrow(new RuntimeException("fk")).when(paymentRepository).deleteById(PAYMENT_ID);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error deleting Payment").contains("fk");
        verify(paymentRepository, times(1)).existsById(PAYMENT_ID);
        verify(paymentRepository, times(1)).deleteById(PAYMENT_ID);
        verifyNoMoreInteractions(paymentRepository);
        verifyNoInteractions(orderRepository);
    }
}
