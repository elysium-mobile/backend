package pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreatePaymentCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdatePaymentCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects.PaymentStatus;
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

    @Getter
    @Column(name = "payment_status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Getter
    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

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
        this.paymentStatus = command.paymentStatus();
        this.paymentMethod = command.paymentMethod();
    }

    /**
     * Updates the Payment with details from an UpdatePaymentCommand.
     * @param command the command containing updated payment details
     */
    public void updatePayment(UpdatePaymentCommand command) {
        this.orderId = command.orderId();
        this.transactionId = command.transactionId();
        this.paymentDate = command.paymentDate();
        this.paymentStatus = command.paymentStatus();
        this.paymentMethod = command.paymentMethod();
    }

    /**
     * Mark the payment as succeeded.
     * @param paymentMethod the method used for the payment (e.g., "card")
     */
    public void markAsSucceeded(String paymentMethod) {
        this.paymentStatus = PaymentStatus.SUCCEEDED;
        this.paymentMethod = paymentMethod;
    }

    /**
     * Mark the payment as failed.
     */
    public void markAsFailed() {
        this.paymentStatus = PaymentStatus.FAILED;
    }

    /**
     * Mark the payment as refunded.
     */
    public void markAsRefunded() {
        this.paymentStatus = PaymentStatus.REFUNDED;
    }

    /**
     * Check if the payment has been completed successfully.
     * @return true if payment status is SUCCEEDED
     */
    public boolean isSucceeded() {
        return this.paymentStatus == PaymentStatus.SUCCEEDED;
    }

    /**
     * Check if the payment has failed.
     * @return true if payment status is FAILED
     */
    public boolean isFailed() {
        return this.paymentStatus == PaymentStatus.FAILED;
    }
}
