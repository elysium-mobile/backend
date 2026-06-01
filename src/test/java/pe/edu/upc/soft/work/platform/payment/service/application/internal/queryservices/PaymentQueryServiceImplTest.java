package pe.edu.upc.soft.work.platform.payment.service.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates.Payment;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetAllPaymentQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetPaymentByIdQuery;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.PaymentRepository;
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
class PaymentQueryServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentQueryServiceImpl service;

    private static Payment sample() {
        return new Payment(PaymentCommandFixtures.validCreatePaymentCommand());
    }

    @Test
    @DisplayName("handle(GetAllPaymentQuery) -> returns list from repository (AAA)")
    void handleAllReturnsList() {
        // Arrange
        List<Payment> payments = List.of(sample());
        when(paymentRepository.findAll()).thenReturn(payments);

        // Act
        List<Payment> result = service.handle(new GetAllPaymentQuery());

        // Assert
        assertThat(result).containsExactlyElementsOf(payments);
        verify(paymentRepository, times(1)).findAll();
        verifyNoMoreInteractions(paymentRepository);
    }

    @Test
    @DisplayName("handle(GetAllPaymentQuery) -> returns empty list when none exist (AAA)")
    void handleAllReturnsEmpty() {
        // Arrange
        when(paymentRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Payment> result = service.handle(new GetAllPaymentQuery());

        // Assert
        assertThat(result).isEmpty();
        verify(paymentRepository, times(1)).findAll();
        verifyNoMoreInteractions(paymentRepository);
    }

    @Test
    @DisplayName("handle(GetPaymentByIdQuery) -> returns Optional with Payment when found (AAA)")
    void handleByIdReturnsPresent() {
        // Arrange
        var payment = sample();
        when(paymentRepository.findById(51L)).thenReturn(Optional.of(payment));

        // Act
        Optional<Payment> result = service.handle(new GetPaymentByIdQuery(51L));

        // Assert
        assertThat(result).isPresent().containsSame(payment);
        verify(paymentRepository, times(1)).findById(51L);
        verifyNoMoreInteractions(paymentRepository);
    }

    @Test
    @DisplayName("handle(GetPaymentByIdQuery) -> returns Optional.empty when no Payment found (AAA)")
    void handleByIdReturnsEmpty() {
        // Arrange
        when(paymentRepository.findById(51L)).thenReturn(Optional.empty());

        // Act
        Optional<Payment> result = service.handle(new GetPaymentByIdQuery(51L));

        // Assert
        assertThat(result).isEmpty();
        verify(paymentRepository, times(1)).findById(51L);
        verifyNoMoreInteractions(paymentRepository);
    }
}
