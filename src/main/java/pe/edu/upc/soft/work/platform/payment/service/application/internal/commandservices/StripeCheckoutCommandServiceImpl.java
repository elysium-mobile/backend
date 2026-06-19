package pe.edu.upc.soft.work.platform.payment.service.application.internal.commandservices;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateStripeCheckoutCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.StripeCheckoutCommandService;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.OrderRepository;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.stripe.StripeProperties;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;

/**
 * StripeCheckoutCommandServiceImpl
 * Creates a Stripe PaymentIntent for a given Order.
 * The clientSecret returned here is sent to the frontend so that
 * Stripe.js can confirm the payment without the secret key leaving the server.
 */
@Service
public class StripeCheckoutCommandServiceImpl implements StripeCheckoutCommandService {

    private final OrderRepository orderRepository;
    private final StripeProperties stripeProperties;

    /**
     * Constructor for StripeCheckoutCommandServiceImpl.
     * @param orderRepository  repository to look up the Order being charged
     * @param stripeProperties configuration properties holding the Stripe secret key
     */
    public StripeCheckoutCommandServiceImpl(OrderRepository orderRepository,
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
     * Creates a Stripe PaymentIntent and returns the clientSecret.
     * The Order ID is stored in the PaymentIntent metadata so that the
     * webhook handler can correlate the confirmation back to the Order.
     *
     * @param command the command carrying the Order ID and desired currency
     * @return Stripe PaymentIntent clientSecret
     */
    @Override
    public String handle(CreateStripeCheckoutCommand command) {
        var order = orderRepository.findById(command.orderId())
                .orElseThrow(() -> new NotFoundArgumentException(
                        String.format("[StripeCheckoutCommandServiceImpl] Order ID: %s not found",
                                command.orderId())));

        var currency = (command.currency() != null && !command.currency().isBlank()) ? command.currency().toLowerCase() : "usd";

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

            var paymentIntent = PaymentIntent.create(params);
            return paymentIntent.getClientSecret();

        } catch (StripeException e) {
            throw new RuntimeException(
                    "[StripeCheckoutCommandServiceImpl] Failed to create Stripe PaymentIntent: "
                            + e.getMessage(), e);
        }
    }
}
