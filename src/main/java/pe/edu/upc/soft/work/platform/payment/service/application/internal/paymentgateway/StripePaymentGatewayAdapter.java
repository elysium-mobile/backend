package pe.edu.upc.soft.work.platform.payment.service.application.internal.paymentgateway;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateStripeCheckoutSessionCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.PaymentGatewayAdapter;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.OrderRepository;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.stripe.StripeProperties;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;

import java.util.List;

/**
 * StripePaymentGatewayAdapter
 * Concrete implementation of PaymentGatewayAdapter for Stripe payment processing.
 * Handles creation of Stripe Checkout Sessions — the hosted payment page that
 * Stripe manages end-to-end. The frontend only needs the returned checkoutUrl
 * to redirect the customer.
 */
@Service
public class StripePaymentGatewayAdapter implements PaymentGatewayAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(StripePaymentGatewayAdapter.class);

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
     * Creates a Stripe Checkout Session for the given Order.
     * The session is configured with:
     * <ul>
     *   <li>A single line item matching the Order amount</li>
     *   <li>Metadata to correlate orderId and membershipId on the webhook side</li>
     *   <li>PaymentIntent-level metadata so that the payment_intent.succeeded
     *       webhook can still resolve the internal Order ID</li>
     *   <li>successUrl and cancelUrl from the command (or from properties as fallback)</li>
     * </ul>
     *
     * @param command the command carrying the Order ID, currency, and redirect URLs
     * @return CheckoutSessionResponse with checkoutUrl, sessionId, and paymentIntentId
     * @throws NotFoundArgumentException if the Order is not found
     * @throws IllegalArgumentException  if URLs are invalid or configuration is missing
     * @throws RuntimeException          if the Stripe API call fails
     */
    @Override
    public CheckoutSessionResponse createCheckoutSession(CreateStripeCheckoutSessionCommand command) {
        var order = orderRepository.findById(command.orderId())
            .orElseThrow(() -> new NotFoundArgumentException(
                String.format("[StripePaymentGatewayAdapter] Order ID: %s not found",
                    command.orderId())));

        // Validate metadata values before paying Stripe API costs
        var orderId = command.orderId();
        var membershipId = order.getMembershipId();
        if (orderId == null) {
            throw new IllegalArgumentException("[StripePaymentGatewayAdapter] orderId must not be null");
        }
        if (membershipId == null) {
            throw new IllegalArgumentException("[StripePaymentGatewayAdapter] Order " + orderId + " has no membershipId");
        }

        var currency = (command.currency() != null && !command.currency().isBlank())
            ? command.currency().toLowerCase()
            : "usd";

        var successUrl = resolveSuccessUrl(command);
        if (successUrl == null || successUrl.isBlank()) {
            throw new IllegalArgumentException(
                "[StripePaymentGatewayAdapter] successUrl is required. Set stripe.success-url in properties "
                    + "or pass successUrl in the request body.");
        }

        var cancelUrl = resolveCancelUrl(command);
        if (cancelUrl == null || cancelUrl.isBlank()) {
            throw new IllegalArgumentException(
                "[StripePaymentGatewayAdapter] cancelUrl is required. Set stripe.cancel-url in properties "
                    + "or pass cancelUrl in the request body.");
        }

        try {
            var lineItem = SessionCreateParams.LineItem.builder()
                .setPriceData(
                    SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(currency)
                        .setProductData(
                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName("Order #" + order.getId())
                                .build())
                        .setUnitAmount((long) order.getAmount() * 100L)
                        .build())
                .setQuantity(1L)
                .build();

            var params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .addAllLineItem(List.of(lineItem))
                .putMetadata("orderId", String.valueOf(orderId))
                .putMetadata("membershipId", String.valueOf(membershipId))
                .setPaymentIntentData(
                    SessionCreateParams.PaymentIntentData.builder()
                        .putMetadata("orderId", String.valueOf(orderId))
                        .putMetadata("membershipId", String.valueOf(membershipId))
                        .build())
                .build();

            var requestOptions = RequestOptions.builder()
                .setIdempotencyKey(command.idempotencyKey())
                .build();

            var session = Session.create(params, requestOptions);

            var checkoutUrl = session.getUrl();
            var sessionId = session.getId();
            var paymentIntentId = session.getPaymentIntent();

            if (checkoutUrl == null || checkoutUrl.isBlank()) {
                throw new RuntimeException(
                    "[StripePaymentGatewayAdapter] Stripe returned a Session without a URL. Session ID: " + sessionId);
            }
            if (paymentIntentId == null || paymentIntentId.isBlank()) {
                LOGGER.warn("[StripePaymentGatewayAdapter] Session {} has no PaymentIntent yet. "
                    + "This is possible before payment completes but unexpected immediately after creation.",
                    sessionId);
            }

            return new CheckoutSessionResponse(checkoutUrl, sessionId, paymentIntentId);

        } catch (StripeException e) {
            throw new RuntimeException(
                "[StripePaymentGatewayAdapter] Failed to create Stripe Checkout Session: "
                    + e.getMessage(), e);
        }
    }

    /**
     * Resolves the success URL from the command or falls back to the configured default.
     * The {@code {CHECKOUT_SESSION_ID}} template variable is appended for Stripe substitution.
     */
    private String resolveSuccessUrl(CreateStripeCheckoutSessionCommand command) {
        if (command.successUrl() != null && !command.successUrl().isBlank()) {
            return command.successUrl();
        }
        var defaultUrl = stripeProperties.getSuccessUrl();
        if (defaultUrl != null && !defaultUrl.isBlank()) {
            return defaultUrl.contains("?")
                ? defaultUrl + "&session_id={CHECKOUT_SESSION_ID}"
                : defaultUrl + "?session_id={CHECKOUT_SESSION_ID}";
        }
        return null;
    }

    /**
     * Resolves the cancel URL from the command or falls back to the configured default.
     */
    private String resolveCancelUrl(CreateStripeCheckoutSessionCommand command) {
        if (command.cancelUrl() != null && !command.cancelUrl().isBlank()) {
            return command.cancelUrl();
        }
        return stripeProperties.getCancelUrl();
    }
}
