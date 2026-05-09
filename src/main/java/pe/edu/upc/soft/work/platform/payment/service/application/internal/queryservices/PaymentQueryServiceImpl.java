package pe.edu.upc.soft.work.platform.payment.service.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates.Payment;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetPaymentByIdQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetAllPaymentQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.PaymentQueryService;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.PaymentRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the PaymentQueryService interface.
 */
@Service
public class PaymentQueryServiceImpl implements PaymentQueryService {
    private final PaymentRepository paymentRepository;

    /**
     * Constructor for PaymentQueryServiceImpl.
     */
    public PaymentQueryServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Handles the GetAllPaymentQuery.
     */
    @Override
    public List<Payment> handle(GetAllPaymentQuery query) {
        return paymentRepository.findAll();
    }

    /**
     * Handles the GetPaymentByIdQuery.
     */
    @Override
    public Optional<Payment> handle(GetPaymentByIdQuery query) {
        return paymentRepository.findById(query.paymentId());
    }
}
