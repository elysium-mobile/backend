package pe.edu.upc.soft.work.platform.payment.service.infrastructure.stripe;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * StripeProperties
 * Binds the Stripe API keys, webhook secret, and Checkout Session URLs from application.properties.
 * Validates all required values at startup to fail fast in case of misconfiguration.
 *
 * Required entries in application.properties:
 *   stripe.secret-key=sk_test_... (dev) or sk_live_... (prod)
 *   stripe.webhook-secret=whsec_...
 *   stripe.success-url=https://frontend.example.com/payment/success
 *   stripe.cancel-url=https://frontend.example.com/payment/cancel
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "stripe")
public class StripeProperties {

    private static final Logger LOGGER = LoggerFactory.getLogger(StripeProperties.class);

    private String secretKey;
    private String webhookSecret;
    private String successUrl;
    private String cancelUrl;

    /**
     * Validates that all required Stripe properties are present and
     * that the environment (test vs. live) is consistent with the
     * active Spring profile. Fails fast at application startup so
     * misconfigurations are caught before any payment request arrives.
     */
    @PostConstruct
    public void validate() {
        List<String> errors = new ArrayList<>();

        if (secretKey == null || secretKey.isBlank()) {
            errors.add("stripe.secret-key is required");
        }
        if (webhookSecret == null || webhookSecret.isBlank()) {
            errors.add("stripe.webhook-secret is required");
        }
        if (successUrl == null || successUrl.isBlank()) {
            errors.add("stripe.success-url is required");
        } else if (!successUrl.startsWith("https://") && !successUrl.startsWith("http://localhost")) {
            errors.add("stripe.success-url must use HTTPS (or http://localhost for development)");
        }
        if (cancelUrl == null || cancelUrl.isBlank()) {
            errors.add("stripe.cancel-url is required");
        } else if (!cancelUrl.startsWith("https://") && !cancelUrl.startsWith("http://localhost")) {
            errors.add("stripe.cancel-url must use HTTPS (or http://localhost for development)");
        }

        // Environment key validation: dev/test profiles must use sk_test_ keys,
        // and production profile must use sk_live_ keys.
        String activeProfile = System.getProperty("spring.profiles.active", "dev");
        if (secretKey != null) {
            boolean isTestKey = secretKey.startsWith("sk_test_");
            boolean isLiveKey = secretKey.startsWith("sk_live_");
            boolean isDevProfile = "dev".equalsIgnoreCase(activeProfile) || "test".equalsIgnoreCase(activeProfile);
            boolean isProdProfile = "prod".equalsIgnoreCase(activeProfile);

            if (isDevProfile && isLiveKey) {
                errors.add("stripe.secret-key is a LIVE key but the active profile is '" + activeProfile + "'. "
                    + "Use a test key (sk_test_...) for development.");
            }
            if (isProdProfile && isTestKey) {
                errors.add("stripe.secret-key is a TEST key but the active profile is '" + activeProfile + "'. "
                    + "Use a live key (sk_live_...) for production.");
            }
        }

        if (!errors.isEmpty()) {
            for (String error : errors) {
                LOGGER.error("[StripeProperties] Validation error: {}", error);
            }
            throw new IllegalStateException(
                "Stripe configuration validation failed. " + String.join("; ", errors));
        }

        LOGGER.info("[StripeProperties] All Stripe properties validated successfully for profile '{}'", activeProfile);
    }
}
