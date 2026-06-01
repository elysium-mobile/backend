package pe.edu.upc.soft.work.platform.payment.service.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.Order;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetAllOrderQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetOrderByIdQuery;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.OrderRepository;
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
class OrderQueryServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderQueryServiceImpl service;

    private static Order sample() {
        return new Order(PaymentCommandFixtures.validCreateOrderCommand());
    }

    @Test
    @DisplayName("handle(GetAllOrderQuery) -> returns list from repository (AAA)")
    void handleAllReturnsList() {
        // Arrange
        List<Order> orders = List.of(sample());
        when(orderRepository.findAll()).thenReturn(orders);

        // Act
        List<Order> result = service.handle(new GetAllOrderQuery());

        // Assert
        assertThat(result).containsExactlyElementsOf(orders);
        verify(orderRepository, times(1)).findAll();
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    @DisplayName("handle(GetAllOrderQuery) -> returns empty list when none exist (AAA)")
    void handleAllReturnsEmpty() {
        // Arrange
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Order> result = service.handle(new GetAllOrderQuery());

        // Assert
        assertThat(result).isEmpty();
        verify(orderRepository, times(1)).findAll();
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    @DisplayName("handle(GetOrderByIdQuery) -> returns Optional with Order when found (AAA)")
    void handleByIdReturnsPresent() {
        // Arrange
        var order = sample();
        when(orderRepository.findById(41L)).thenReturn(Optional.of(order));

        // Act
        Optional<Order> result = service.handle(new GetOrderByIdQuery(41L));

        // Assert
        assertThat(result).isPresent().containsSame(order);
        verify(orderRepository, times(1)).findById(41L);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    @DisplayName("handle(GetOrderByIdQuery) -> returns Optional.empty when no Order found (AAA)")
    void handleByIdReturnsEmpty() {
        // Arrange
        when(orderRepository.findById(41L)).thenReturn(Optional.empty());

        // Act
        Optional<Order> result = service.handle(new GetOrderByIdQuery(41L));

        // Assert
        assertThat(result).isEmpty();
        verify(orderRepository, times(1)).findById(41L);
        verifyNoMoreInteractions(orderRepository);
    }
}
