package pe.edu.upc.soft.work.platform.payment.service.application.internal.eventhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.events.PaymentRegisteredEvent;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetPaymentByIdQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.PaymentQueryService;

/**
 * Event handler responsible for reacting to a successful PaymentRegisteredEvent.
 */
@Service
public class PaymentRegisteredEventHandler {

    private final PaymentQueryService paymentQueryService;
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentRegisteredEventHandler.class);

    /**
     * Constructor for PaymentRegisteredEventHandler.
     * @param paymentQueryService service to query the Payment aggregate
     */
    public PaymentRegisteredEventHandler(PaymentQueryService paymentQueryService) {
        this.paymentQueryService = paymentQueryService;
    }

    /**
     * Handles the PaymentRegisteredEvent after a new payment has been successfully registered.
     * @param event the PaymentRegisteredEvent containing payment and order IDs
     */
    @EventListener
    public void on(PaymentRegisteredEvent event) {
        var getPaymentByIdQuery = new GetPaymentByIdQuery(event.getPaymentId());
        var payment = paymentQueryService.handle(getPaymentByIdQuery);

        if (payment.isPresent()) {
            LOGGER.info("Payment successfully registered with ID: {} for Order ID: {}",
                    event.getPaymentId(), event.getOrderId());
        } else {
            LOGGER.warn("Error: Payment with ID {} could not be found after registration.", event.getPaymentId());
        }
    }
}
