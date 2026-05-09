package pe.edu.upc.soft.work.platform.payment.service.domain.services;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates.Payment;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetPaymentByIdQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetAllPaymentQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying Payments in the system.
 */
public interface PaymentQueryService {

    /**
     * Retrieves a list of all Payments in the system.
     */
    List<Payment> handle(GetAllPaymentQuery query);

    /**
     * Retrieves a Payment by their unique identifier.
     */
    Optional<Payment> handle(GetPaymentByIdQuery query);
}
