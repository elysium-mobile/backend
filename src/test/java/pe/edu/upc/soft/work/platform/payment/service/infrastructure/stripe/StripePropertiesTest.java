package pe.edu.upc.soft.work.platform.payment.service.infrastructure.stripe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StripePropertiesTest {

    @Test
    @DisplayName("validate() -> throws when secretKey is null")
    void failsWhenSecretKeyIsNull() {
        var props = new StripeProperties();
        props.setWebhookSecret("whsec_test");
        props.setSuccessUrl("https://example.com/success");
        props.setCancelUrl("https://example.com/cancel");

        assertThatThrownBy(props::validate)
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("stripe.secret-key");
    }

    @Test
    @DisplayName("validate() -> throws when webhookSecret is null")
    void failsWhenWebhookSecretIsNull() {
        var props = new StripeProperties();
        props.setSecretKey("sk_test_abc");
        props.setSuccessUrl("https://example.com/success");
        props.setCancelUrl("https://example.com/cancel");

        assertThatThrownBy(props::validate)
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("stripe.webhook-secret");
    }

    @Test
    @DisplayName("validate() -> throws when successUrl is null")
    void failsWhenSuccessUrlIsNull() {
        var props = new StripeProperties();
        props.setSecretKey("sk_test_abc");
        props.setWebhookSecret("whsec_test");
        props.setCancelUrl("https://example.com/cancel");

        assertThatThrownBy(props::validate)
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("stripe.success-url");
    }

    @Test
    @DisplayName("validate() -> throws when cancelUrl is null")
    void failsWhenCancelUrlIsNull() {
        var props = new StripeProperties();
        props.setSecretKey("sk_test_abc");
        props.setWebhookSecret("whsec_test");
        props.setSuccessUrl("https://example.com/success");

        assertThatThrownBy(props::validate)
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("stripe.cancel-url");
    }

    @Test
    @DisplayName("validate() -> throws when successUrl uses plain http without localhost")
    void failsWhenSuccessUrlIsHttp() {
        var props = new StripeProperties();
        props.setSecretKey("sk_test_abc");
        props.setWebhookSecret("whsec_test");
        props.setSuccessUrl("http://example.com/success");
        props.setCancelUrl("https://example.com/cancel");

        assertThatThrownBy(props::validate)
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("HTTPS");
    }

    @Test
    @DisplayName("validate() -> passes when all properties are valid")
    void passesWithValidProperties() {
        var props = new StripeProperties();
        props.setSecretKey("sk_test_abc");
        props.setWebhookSecret("whsec_test");
        props.setSuccessUrl("https://example.com/success");
        props.setCancelUrl("https://example.com/cancel");

        // Should not throw
        props.validate();
    }
}
