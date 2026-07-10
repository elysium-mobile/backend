package pe.edu.upc.soft.work.platform.payment.service.application.internal.paymentgateway;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateStripeCheckoutCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.PaymentGatewayAdapter;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.OrderRepository;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.stripe.StripeProperties;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;

/**
 * StripePaymentGatewayAdapter
 * Concrete implementation of PaymentGatewayAdapter for Stripe payment processing.
 * Handles creation of Stripe PaymentIntent and extracts necessary payment information.
 */
@Service
public class StripePaymentGatewayAdapter implements PaymentGatewayAdapter {

    private final OrderRepository orderRepository;
    private final StripeProperties stripeProperties;

    /**
     * Constructor for StripePaymentGatewayAdapter.
     * @param orderRepository  repository to look up the Order being charged
     * @param stripeProperties configuration properties holding the Stripe secret key
     */
    public StripePaymentGatewayAdapter(OrderRepository orderRepository,
                                       StripeProperties stripeProperties) {
        this.orderRepository = orderRepository;
        this.stripeProperties = stripeProperties;
    }

    /**
     * Initialises the Stripe SDK with the secret key once the bean is ready.
     */
    @PostConstruct
    public void initStripe() {
        Stripe.apiKey = stripeProperties.getSecretKey();
    }

    /**
     * Creates a Stripe PaymentIntent for the given Order and returns the response.
     * The Order ID is stored in the PaymentIntent metadata so that the
     * webhook handler can correlate the confirmation back to the Order.
     *
     * @param command the command carrying the Order ID and desired currency
     * @return PaymentGatewayResponse with clientSecret and transactionId
     */
    @Override
    public PaymentGatewayResponse createPaymentIntent(CreateStripeCheckoutCommand command) {
        var order = orderRepository.findById(command.orderId())
            .orElseThrow(() -> new NotFoundArgumentException(
                String.format("[StripePaymentGatewayAdapter] Order ID: %s not found",
                    command.orderId())));

        var currency = (command.currency() != null && !command.currency().isBlank())
            ? command.currency().toLowerCase()
            : "usd";

        try {
            var params = PaymentIntentCreateParams.builder()
                .setAmount((long) order.getAmount() * 100L)
                .setCurrency(currency)
                .setAutomaticPaymentMethods(
                    PaymentIntentCreateParams
                        .AutomaticPaymentMethods.builder()
                        .setEnabled(true)
                        .build())
                .putMetadata("orderId", String.valueOf(command.orderId()))
                .putMetadata("membershipId", String.valueOf(order.getMembershipId()))
                .build();

            var requestOptions = RequestOptions.builder()
                .setIdempotencyKey(command.idempotencyKey())
                .build();

            var paymentIntent = PaymentIntent.create(params, requestOptions);

            return new PaymentGatewayResponse(
                paymentIntent.getClientSecret(),
                paymentIntent.getId(),
                "CREDIT_CARD"  // Default payment method; could be more dynamic
            );

        } catch (StripeException e) {
            throw new RuntimeException(
                "[StripePaymentGatewayAdapter] Failed to create Stripe PaymentIntent: "
                    + e.getMessage(), e);
        }
    }
}
