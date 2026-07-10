package pe.edu.upc.soft.work.platform.payment.service.application.internal.paymentgateway;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.param.checkout.SessionCreateParams;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateStripeCheckoutSessionCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.Order;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.PaymentGatewayAdapter.CheckoutSessionResponse;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.OrderRepository;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.stripe.StripeProperties;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StripePaymentGatewayAdapterTest {

    private static final Long ORDER_ID = 42L;
    private static final Long MEMBERSHIP_ID = 7L;
    private static final int AMOUNT_CENTS = 2999;
    private static final String CURRENCY = "usd";
    private static final String CHECKOUT_URL = "https://checkout.stripe.com/c/pay_cs_test_123";
    private static final String SESSION_ID = "cs_test_123";
    private static final String PAYMENT_INTENT_ID = "pi_test_123";

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private StripeProperties stripeProperties;

    @InjectMocks
    private StripePaymentGatewayAdapter adapter;

    @Captor
    private ArgumentCaptor<SessionCreateParams> paramsCaptor;

    @Captor
    private ArgumentCaptor<RequestOptions> optionsCaptor;

    private Order createOrder() {
        var order = new Order();
        ReflectionTestUtils.setId(order, ORDER_ID);
        order.setMembershipId(MEMBERSHIP_ID);
        ReflectionTestUtils.setField(order, "amount", AMOUNT_CENTS);
        return order;
    }

    @Test
    @DisplayName("createCheckoutSession -> builds SessionCreateParams and returns CheckoutSessionResponse")
    void createsCheckoutSessionSuccessfully() throws StripeException {
        var order = createOrder();
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));

        var command = new CreateStripeCheckoutSessionCommand(
            ORDER_ID, CURRENCY, "https://example.com/success", "https://example.com/cancel");

        var mockSession = mock(Session.class);
        when(mockSession.getUrl()).thenReturn(CHECKOUT_URL);
        when(mockSession.getId()).thenReturn(SESSION_ID);
        when(mockSession.getPaymentIntent()).thenReturn(PAYMENT_INTENT_ID);

        try (var mockedSession = mockStatic(Session.class)) {
            mockedSession.when(() -> Session.create(any(SessionCreateParams.class), any(RequestOptions.class)))
                .thenReturn(mockSession);

            CheckoutSessionResponse response = adapter.createCheckoutSession(command);

            assertThat(response.checkoutUrl()).isEqualTo(CHECKOUT_URL);
            assertThat(response.sessionId()).isEqualTo(SESSION_ID);
            assertThat(response.paymentIntentId()).isEqualTo(PAYMENT_INTENT_ID);

            mockedSession.verify(() -> Session.create(paramsCaptor.capture(), optionsCaptor.capture()));
        }

        var params = paramsCaptor.getValue();
        assertThat(params.getMode()).isEqualTo(SessionCreateParams.Mode.PAYMENT);
        assertThat(params.getSuccessUrl()).contains("example.com/success");
        assertThat(params.getCancelUrl()).contains("example.com/cancel");
        assertThat(params.getMetadata()).containsEntry("orderId", "42");
        assertThat(params.getMetadata()).containsEntry("membershipId", "7");
    }

    @Test
    @DisplayName("createCheckoutSession -> uses default URLs from properties when command URLs are null")
    void usesDefaultUrlsWhenCommandUrlsAreNull() throws StripeException {
        var order = createOrder();
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
        when(stripeProperties.getSuccessUrl()).thenReturn("https://frontend.test/success");
        when(stripeProperties.getCancelUrl()).thenReturn("https://frontend.test/cancel");

        var command = new CreateStripeCheckoutSessionCommand(
            ORDER_ID, CURRENCY, java.util.UUID.randomUUID().toString(), null, null);

        var mockSession = mock(Session.class);
        when(mockSession.getUrl()).thenReturn(CHECKOUT_URL);
        when(mockSession.getId()).thenReturn(SESSION_ID);
        when(mockSession.getPaymentIntent()).thenReturn(PAYMENT_INTENT_ID);

        try (var mockedSession = mockStatic(Session.class)) {
            mockedSession.when(() -> Session.create(any(SessionCreateParams.class), any(RequestOptions.class)))
                .thenReturn(mockSession);

            adapter.createCheckoutSession(command);

            mockedSession.verify(() -> Session.create(paramsCaptor.capture(), any()));
        }

        var params = paramsCaptor.getValue();
        assertThat(params.getSuccessUrl()).contains("frontend.test/success");
        assertThat(params.getCancelUrl()).contains("frontend.test/cancel");
    }

    @Test
    @DisplayName("createCheckoutSession -> throws IllegalArgumentException when successUrl is missing")
    void throwsWhenSuccessUrlMissing() {
        var order = createOrder();
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
        when(stripeProperties.getSuccessUrl()).thenReturn(null);

        var command = new CreateStripeCheckoutSessionCommand(
            ORDER_ID, CURRENCY, "retry-key", null, "https://example.com/cancel");

        var ex = assertThrows(IllegalArgumentException.class,
            () -> adapter.createCheckoutSession(command));
        assertThat(ex.getMessage()).contains("successUrl");
    }

    @Test
    @DisplayName("createCheckoutSession -> throws IllegalArgumentException when cancelUrl is missing")
    void throwsWhenCancelUrlMissing() {
        var order = createOrder();
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
        when(stripeProperties.getSuccessUrl()).thenReturn("https://example.com/success");
        when(stripeProperties.getCancelUrl()).thenReturn(null);

        var command = new CreateStripeCheckoutSessionCommand(
            ORDER_ID, CURRENCY, "retry-key", null, null);

        var ex = assertThrows(IllegalArgumentException.class,
            () -> adapter.createCheckoutSession(command));
        assertThat(ex.getMessage()).contains("cancelUrl");
    }

    @Test
    @DisplayName("createCheckoutSession -> throws NotFoundArgumentException when Order is missing")
    void throwsWhenOrderNotFound() {
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.empty());

        var command = new CreateStripeCheckoutSessionCommand(
            ORDER_ID, CURRENCY, "https://example.com/success", "https://example.com/cancel");

        assertThrows(NotFoundArgumentException.class,
            () -> adapter.createCheckoutSession(command));
    }

    @Test
    @DisplayName("createCheckoutSession -> throws IllegalArgumentException when membershipId is null")
    void throwsWhenMembershipIdIsNull() {
        var order = createOrder();
        ReflectionTestUtils.setField(order, "membershipId", null);
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));

        var command = new CreateStripeCheckoutSessionCommand(
            ORDER_ID, CURRENCY, "https://example.com/success", "https://example.com/cancel");

        var ex = assertThrows(IllegalArgumentException.class,
            () -> adapter.createCheckoutSession(command));
        assertThat(ex.getMessage()).contains("membershipId");
    }

    @Test
    @DisplayName("createCheckoutSession -> uses default currency 'usd' when currency is blank")
    void usesDefaultCurrencyWhenBlank() throws StripeException {
        var order = createOrder();
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
        when(stripeProperties.getSuccessUrl()).thenReturn("https://frontend.test/success");
        when(stripeProperties.getCancelUrl()).thenReturn("https://frontend.test/cancel");

        var command = new CreateStripeCheckoutSessionCommand(
            ORDER_ID, "", java.util.UUID.randomUUID().toString(), null, null);

        var mockSession = mock(Session.class);
        when(mockSession.getUrl()).thenReturn(CHECKOUT_URL);
        when(mockSession.getId()).thenReturn(SESSION_ID);
        when(mockSession.getPaymentIntent()).thenReturn(PAYMENT_INTENT_ID);

        try (var mockedSession = mockStatic(Session.class)) {
            mockedSession.when(() -> Session.create(any(SessionCreateParams.class), any(RequestOptions.class)))
                .thenReturn(mockSession);

            adapter.createCheckoutSession(command);

            mockedSession.verify(() -> Session.create(paramsCaptor.capture(), any()));
        }

        assertThat(paramsCaptor.getValue().getLineItems().get(0).getPriceData().getCurrency())
            .isEqualTo("usd");
    }
}
