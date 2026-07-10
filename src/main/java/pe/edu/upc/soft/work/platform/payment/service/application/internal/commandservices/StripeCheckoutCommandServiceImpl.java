package pe.edu.upc.soft.work.platform.payment.service.application.internal.commandservices;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateStripeCheckoutSessionCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.PaymentGatewayAdapter;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.StripeCheckoutCommandService;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.stripe.StripeProperties;

import java.util.List;

/**
 * StripeCheckoutCommandServiceImpl
 * Creates a Stripe Checkout Session for a given Order and returns
 * the hosted checkout URL. The frontend redirects the customer to
 * this URL; Stripe handles the entire payment flow on its hosted page.
 * <p>
 * Delegates the actual Stripe API call to {@link PaymentGatewayAdapter},
 * keeping this service focused on use-case orchestration.
 */
@Service
public class StripeCheckoutCommandServiceImpl implements StripeCheckoutCommandService {

    private final PaymentGatewayAdapter paymentGatewayAdapter;

    /**
     * Constructor for StripeCheckoutCommandServiceImpl.
     * @param paymentGatewayAdapter the adapter that performs the actual Stripe API call
     */
    public StripeCheckoutCommandServiceImpl(PaymentGatewayAdapter paymentGatewayAdapter) {
        this.paymentGatewayAdapter = paymentGatewayAdapter;
    }

    /**
     * Creates a Stripe Checkout Session and returns the hosted checkout URL.
     *
     * @param command the command carrying the Order ID, currency, and redirect URLs
     * @return the hosted Stripe Checkout page URL
     */
    @Override
    public String handle(CreateStripeCheckoutSessionCommand command) {
        var response = paymentGatewayAdapter.createCheckoutSession(command);
        return response.checkoutUrl();
    }
}
