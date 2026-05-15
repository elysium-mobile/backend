package pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreatePaymentCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdatePaymentCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import java.util.Date;

/**
 * Payment aggregate root entity.
 */
@Entity
@Table(name = "payments")
public class Payment extends AuditableAbstractAggregateRoot<Payment> {

    @Getter
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    @Getter
    @Column(name = "transaction_id", nullable = false, length = 50)
    private String transactionId;
    @Getter
    @Column(name = "payment_date", nullable = false)
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
