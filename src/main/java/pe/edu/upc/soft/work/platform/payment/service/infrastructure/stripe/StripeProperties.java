package pe.edu.upc.soft.work.platform.payment.service.infrastructure.stripe;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * StripeProperties
 * Binds the Stripe API keys and webhook secret from application.properties.
 *
 * Required entries in application.properties:
 *   stripe.secret-key=sk_test_...
 *   stripe.webhook-secret=whsec_...
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "stripe")
public class StripeProperties {

    private String secretKey;
    private String webhookSecret;
}
