package pe.edu.upc.soft.work.platform.payment.service.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.payment.service.application.internal.outboundservices.acl.ExternalIamServiceFromPaymentService;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.DeleteOrderCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.Order;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.MembershipRepository;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.OrderRepository;
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
class OrderCommandServiceImplTest {

    private static final Long ORDER_ID = 41L;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ExternalIamServiceFromPaymentService externalIamServiceFromPaymentService;
    @Mock
    private MembershipRepository membershipRepository;

    @InjectMocks
    private OrderCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateOrderCommand) -> creates Order when user and membership exist (AAA)")
    void handleCreateSuccess() {
        // Arrange
        var command = PaymentCommandFixtures.validCreateOrderCommand();
        when(externalIamServiceFromPaymentService.existsUserAccountById(PaymentCommandFixtures.VALID_USER_ACCOUNT_ID))
                .thenReturn(true);
        when(membershipRepository.existsById(PaymentCommandFixtures.VALID_MEMBERSHIP_ID)).thenReturn(true);
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            ReflectionTestUtils.setId(o, ORDER_ID);
            return o;
        });

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(ORDER_ID);
        verify(externalIamServiceFromPaymentService, times(1))
                .existsUserAccountById(PaymentCommandFixtures.VALID_USER_ACCOUNT_ID);
        verify(membershipRepository, times(1)).existsById(PaymentCommandFixtures.VALID_MEMBERSHIP_ID);
        verify(orderRepository, times(1)).save(any(Order.class));
        verifyNoMoreInteractions(externalIamServiceFromPaymentService, membershipRepository, orderRepository);
    }

    @Test
    @DisplayName("handle(CreateOrderCommand) -> throws NotFoundArgumentException when user account is missing (AAA)")
    void handleCreateMissingUser() {
        // Arrange
        var command = PaymentCommandFixtures.validCreateOrderCommand();
        when(externalIamServiceFromPaymentService.existsUserAccountById(PaymentCommandFixtures.VALID_USER_ACCOUNT_ID))
                .thenReturn(false);

        // Act + Assert
        NotFoundArgumentException ex = assertThrows(NotFoundArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("User Account ID: " + PaymentCommandFixtures.VALID_USER_ACCOUNT_ID);
        verify(externalIamServiceFromPaymentService, times(1))
                .existsUserAccountById(PaymentCommandFixtures.VALID_USER_ACCOUNT_ID);
        verifyNoMoreInteractions(externalIamServiceFromPaymentService);
        verifyNoInteractions(membershipRepository, orderRepository);
    }

    @Test
    @DisplayName("handle(CreateOrderCommand) -> throws NotFoundArgumentException when membership is missing (AAA)")
    void handleCreateMissingMembership() {
        // Arrange
        var command = PaymentCommandFixtures.validCreateOrderCommand();
        when(externalIamServiceFromPaymentService.existsUserAccountById(PaymentCommandFixtures.VALID_USER_ACCOUNT_ID))
                .thenReturn(true);
        when(membershipRepository.existsById(PaymentCommandFixtures.VALID_MEMBERSHIP_ID)).thenReturn(false);

        // Act + Assert
        NotFoundArgumentException ex = assertThrows(NotFoundArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Membership ID: " + PaymentCommandFixtures.VALID_MEMBERSHIP_ID);
        verify(externalIamServiceFromPaymentService, times(1))
                .existsUserAccountById(PaymentCommandFixtures.VALID_USER_ACCOUNT_ID);
        verify(membershipRepository, times(1)).existsById(PaymentCommandFixtures.VALID_MEMBERSHIP_ID);
        verifyNoMoreInteractions(externalIamServiceFromPaymentService, membershipRepository);
        verifyNoInteractions(orderRepository);
    }

    @Test
    @DisplayName("handle(CreateOrderCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleCreateSaveFailure() {
        // Arrange
        var command = PaymentCommandFixtures.validCreateOrderCommand();
        when(externalIamServiceFromPaymentService.existsUserAccountById(PaymentCommandFixtures.VALID_USER_ACCOUNT_ID))
                .thenReturn(true);
        when(membershipRepository.existsById(PaymentCommandFixtures.VALID_MEMBERSHIP_ID)).thenReturn(true);
        when(orderRepository.save(any(Order.class))).thenThrow(new RuntimeException("db"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error creating Order").contains("db");
        verify(externalIamServiceFromPaymentService, times(1))
                .existsUserAccountById(PaymentCommandFixtures.VALID_USER_ACCOUNT_ID);
        verify(membershipRepository, times(1)).existsById(PaymentCommandFixtures.VALID_MEMBERSHIP_ID);
        verify(orderRepository, times(1)).save(any(Order.class));
        verifyNoMoreInteractions(externalIamServiceFromPaymentService, membershipRepository, orderRepository);
    }

    @Test
    @DisplayName("handle(UpdateOrderCommand) -> returns Optional with updated Order when present (AAA)")
    void handleUpdateSuccess() {
        // Arrange
        var existing = new Order(PaymentCommandFixtures.validCreateOrderCommand());
        ReflectionTestUtils.setId(existing, ORDER_ID);
        var command = PaymentCommandFixtures.updateOrderCommand(ORDER_ID);
        when(orderRepository.existsById(ORDER_ID)).thenReturn(true);
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(existing));
        when(orderRepository.save(existing)).thenReturn(existing);

        // Act
        Optional<Order> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getAmount()).isEqualTo(PaymentCommandFixtures.VALID_ORDER_AMOUNT);
        verify(orderRepository, times(1)).existsById(ORDER_ID);
        verify(orderRepository, times(1)).findById(ORDER_ID);
        verify(orderRepository, times(1)).save(existing);
        verifyNoMoreInteractions(orderRepository);
        verifyNoInteractions(externalIamServiceFromPaymentService, membershipRepository);
    }

    @Test
    @DisplayName("handle(UpdateOrderCommand) -> throws RuntimeException when id does not exist (AAA)")
    void handleUpdateMissing() {
        // Arrange
        var command = PaymentCommandFixtures.updateOrderCommand(ORDER_ID);
        when(orderRepository.existsById(ORDER_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(ORDER_ID)).contains("does not exist");
        verify(orderRepository, times(1)).existsById(ORDER_ID);
        verifyNoMoreInteractions(orderRepository);
        verifyNoInteractions(externalIamServiceFromPaymentService, membershipRepository);
    }

    @Test
    @DisplayName("handle(UpdateOrderCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleUpdateSaveFailure() {
        // Arrange
        var existing = new Order(PaymentCommandFixtures.validCreateOrderCommand());
        ReflectionTestUtils.setId(existing, ORDER_ID);
        var command = PaymentCommandFixtures.updateOrderCommand(ORDER_ID);
        when(orderRepository.existsById(ORDER_ID)).thenReturn(true);
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(existing));
        when(orderRepository.save(existing)).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error updating Order").contains("boom");
        verify(orderRepository, times(1)).existsById(ORDER_ID);
        verify(orderRepository, times(1)).findById(ORDER_ID);
        verify(orderRepository, times(1)).save(existing);
        verifyNoMoreInteractions(orderRepository);
        verifyNoInteractions(externalIamServiceFromPaymentService, membershipRepository);
    }

    @Test
    @DisplayName("handle(DeleteOrderCommand) -> deletes when present (AAA)")
    void handleDeleteSuccess() {
        // Arrange
        var command = new DeleteOrderCommand(ORDER_ID);
        when(orderRepository.existsById(ORDER_ID)).thenReturn(true);

        // Act
        service.handle(command);

        // Assert
        verify(orderRepository, times(1)).existsById(ORDER_ID);
        verify(orderRepository, times(1)).deleteById(ORDER_ID);
        verifyNoMoreInteractions(orderRepository);
        verifyNoInteractions(externalIamServiceFromPaymentService, membershipRepository);
    }

    @Test
    @DisplayName("handle(DeleteOrderCommand) -> throws RuntimeException when id is absent (AAA)")
    void handleDeleteMissing() {
        // Arrange
        var command = new DeleteOrderCommand(ORDER_ID);
        when(orderRepository.existsById(ORDER_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(ORDER_ID)).contains("does not exist");
        verify(orderRepository, times(1)).existsById(ORDER_ID);
        verify(orderRepository, never()).deleteById(any(Long.class));
        verifyNoMoreInteractions(orderRepository);
        verifyNoInteractions(externalIamServiceFromPaymentService, membershipRepository);
    }

    @Test
    @DisplayName("handle(DeleteOrderCommand) -> wraps deleteById failure in RuntimeException (AAA)")
    void handleDeleteDeleteFailure() {
        // Arrange
        var command = new DeleteOrderCommand(ORDER_ID);
        when(orderRepository.existsById(ORDER_ID)).thenReturn(true);
        doThrow(new RuntimeException("fk")).when(orderRepository).deleteById(ORDER_ID);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error deleting Order").contains("fk");
        verify(orderRepository, times(1)).existsById(ORDER_ID);
        verify(orderRepository, times(1)).deleteById(ORDER_ID);
        verifyNoMoreInteractions(orderRepository);
        verifyNoInteractions(externalIamServiceFromPaymentService, membershipRepository);
    }
}
