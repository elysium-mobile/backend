package pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates;

import jakarta.persistence.Entity;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreatePaymentCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdatePaymentCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import java.util.Date;

/**
 * Payment aggregate root entity.
 */
@Entity
public class Payment extends AuditableAbstractAggregateRoot<Payment> {

    @Getter
    private Long orderId;
    @Getter
    private String transactionId;
    @Getter
    private Date paymentDate;

    /**
     * Default constructor for JPA.
     */
    public Payment() {}

    /**
     * Constructor to create a Payment from a CreatePaymentCommand.
     * @param command the command containing payment details
     */
    public Payment(CreatePaymentCommand command) {
        this.orderId = command.orderId();
        this.transactionId = command.transactionId();
        this.paymentDate = command.paymentDate();
    }

    /**
     * Updates the Payment with details from an UpdatePaymentCommand.
     * @param command the command containing updated payment details
     */
    public void updatePayment(UpdatePaymentCommand command) {
        this.orderId = command.orderId();
        this.transactionId = command.transactionId();
        this.paymentDate = command.paymentDate();
    }
}
