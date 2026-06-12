package pe.edu.upc.soft.work.platform.payment.service.interfaces.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreatePaymentCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetMembershipByIdQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetPaymentByIdQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.MembershipQueryService;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.PaymentCommandService;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.PaymentQueryService;

import java.util.Date;
import java.util.Objects;

/**
 * Facade for the Payment Service Bounded Context.
 * Exposes payment and membership verification and creation operations
 * for consumption by other Bounded Contexts.
 */
@Service
public class PaymentServiceContextFacade {

    /**
     * Command service for payments.
     */
    private final PaymentCommandService paymentCommandService;

    /**
     * Query service for payments.
     */
    private final PaymentQueryService paymentQueryService;

    /**
     * Query service for memberships.
     */
    private final MembershipQueryService membershipQueryService;

    /**
     * Constructor for PaymentServiceContextFacade.
     *
     * @param paymentCommandService  the payment command service
     * @param paymentQueryService    the payment query service
     * @param membershipQueryService the membership query service
     */
    public PaymentServiceContextFacade(PaymentCommandService paymentCommandService,
                                       PaymentQueryService paymentQueryService,
                                       MembershipQueryService membershipQueryService) {
        this.paymentCommandService = paymentCommandService;
        this.paymentQueryService = paymentQueryService;
        this.membershipQueryService = membershipQueryService;
    }

    /**
     * Check if a payment exists by its ID.
     *
     * @param paymentId the ID of the payment
     * @return true if the payment exists, false otherwise
     */
    public boolean existsPaymentById(Long paymentId) {
        var query = new GetPaymentByIdQuery(paymentId);
        return this.paymentQueryService.handle(query).isPresent();
    }

    /**
     * Check if a membership exists by its ID.
     *
     * @param membershipId the ID of the membership
     * @return true if the membership exists, false otherwise
     */
    public boolean existsMembershipById(Long membershipId) {
        var query = new GetMembershipByIdQuery(membershipId);
        return this.membershipQueryService.handle(query).isPresent();
    }

    /**
     * Create a new payment record.
     *
     * @param orderId       the ID of the associated order
     * @param transactionId the external transaction identifier
     * @param paymentDate   the date of the payment
     * @return the ID of the created payment, or 0L if creation failed
     */
    public Long createPayment(Long orderId, String transactionId, Date paymentDate) {
        var command = new CreatePaymentCommand(orderId, transactionId, paymentDate);
        var paymentId = this.paymentCommandService.handle(command);
        if (Objects.isNull(paymentId)) {
            return 0L;
        }
        return paymentId;
    }
}
