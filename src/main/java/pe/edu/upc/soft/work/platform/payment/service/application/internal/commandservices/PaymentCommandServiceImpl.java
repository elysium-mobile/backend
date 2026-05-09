package pe.edu.upc.soft.work.platform.payment.service.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates.Payment;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreatePaymentCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdatePaymentCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.DeletePaymentCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.PaymentCommandService;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.PaymentRepository;

import java.util.Optional;

@Service
public class PaymentCommandServiceImpl implements PaymentCommandService {
    private final PaymentRepository paymentRepository;

    public PaymentCommandServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Long handle(CreatePaymentCommand command) {
        var payment = new Payment(command);
        try {
            paymentRepository.save(payment);
        } catch (Exception e) {
            throw new RuntimeException("Error creating Payment: " + e.getMessage(), e);
        }
        return payment.getId();
    }

    @Override
    public Optional<Payment> handle(UpdatePaymentCommand command) {
        var paymentId = command.paymentId();
        if (!this.paymentRepository.existsById(paymentId)) {
            throw new RuntimeException("Payment with ID " + paymentId + " does not exist.");
        }

        var paymentToUpdate = this.paymentRepository.findById(paymentId).get();
        paymentToUpdate.updatePayment(command);
        try {
            var updatedPayment = this.paymentRepository.save(paymentToUpdate);
            return Optional.of(updatedPayment);
        } catch (Exception e) {
            throw new RuntimeException("Error updating Payment: " + e.getMessage(), e);
        }
    }

    @Override
    public void handle(DeletePaymentCommand command) {
        if (!paymentRepository.existsById(command.paymentId())) {
            throw new RuntimeException("Payment with ID " + command.paymentId() + " does not exist.");
        }
        try {
            paymentRepository.deleteById(command.paymentId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting Payment: " + e.getMessage(), e);
        }
    }
}
