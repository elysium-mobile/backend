package pe.edu.upc.soft.work.platform.payment.service.test.fixtures;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateBenefitCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateMembershipCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateMembershipPlanCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateOrderCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreatePaymentCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateBenefitCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateMembershipCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateMembershipPlanCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateOrderCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdatePaymentCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects.MembershipStatus;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects.UserAccountId;

import java.util.Date;

/**
 * Payment Service-specific command factories. Mirrors the architectural
 * template established by sibling fixture utilities. Tests MUST NOT
 * instantiate payment commands inline.
 */
public final class PaymentCommandFixtures {

    public static final String VALID_BENEFIT_TITLE = "Premium Support";
    public static final String VALID_BENEFIT_DESCRIPTION = "24/7 priority support";

    public static final Date VALID_MEMBERSHIP_START = new Date(1_700_000_000_000L);
    public static final Date VALID_MEMBERSHIP_OVER = new Date(1_800_000_000_000L);
    public static final MembershipStatus VALID_MEMBERSHIP_STATUS = MembershipStatus.ACTIVE;

    public static final String VALID_PLAN_NAME = "Gold";
    public static final Integer VALID_PLAN_PRICE = 99;

    public static final Long VALID_USER_ACCOUNT_ID = 10L;
    public static final Integer VALID_ORDER_AMOUNT = 250;
    public static final Long VALID_MEMBERSHIP_ID = 5L;

    public static final Long VALID_ORDER_ID = 7L;
    public static final String VALID_TRANSACTION_ID = "TXN-2024-001";
    public static final Date VALID_PAYMENT_DATE = new Date(1_750_000_000_000L);

    private PaymentCommandFixtures() {
        throw new AssertionError("PaymentCommandFixtures is a utility class and must not be instantiated.");
    }

    // ---------- Benefit ----------
    public static CreateBenefitCommand validCreateBenefitCommand() {
        return new CreateBenefitCommand(VALID_BENEFIT_TITLE, VALID_BENEFIT_DESCRIPTION);
    }

    public static UpdateBenefitCommand updateBenefitCommand(Long benefitId) {
        return new UpdateBenefitCommand(benefitId, VALID_BENEFIT_TITLE, VALID_BENEFIT_DESCRIPTION);
    }

    // ---------- Membership ----------
    public static CreateMembershipCommand validCreateMembershipCommand() {
        return new CreateMembershipCommand(VALID_MEMBERSHIP_START, VALID_MEMBERSHIP_OVER, VALID_MEMBERSHIP_STATUS);
    }

    public static UpdateMembershipCommand updateMembershipCommand(Long membershipId) {
        return new UpdateMembershipCommand(membershipId, VALID_MEMBERSHIP_START, VALID_MEMBERSHIP_OVER, VALID_MEMBERSHIP_STATUS);
    }

    // ---------- MembershipPlan ----------
    public static CreateMembershipPlanCommand validCreateMembershipPlanCommand() {
        return new CreateMembershipPlanCommand(VALID_PLAN_NAME, VALID_PLAN_PRICE);
    }

    public static UpdateMembershipPlanCommand updateMembershipPlanCommand(Long planId) {
        return new UpdateMembershipPlanCommand(planId, VALID_PLAN_NAME, VALID_PLAN_PRICE);
    }

    // ---------- Order ----------
    public static CreateOrderCommand validCreateOrderCommand() {
        return new CreateOrderCommand(
                new UserAccountId(VALID_USER_ACCOUNT_ID), VALID_ORDER_AMOUNT, VALID_MEMBERSHIP_ID);
    }

    public static UpdateOrderCommand updateOrderCommand(Long orderId) {
        return new UpdateOrderCommand(
                orderId, new UserAccountId(VALID_USER_ACCOUNT_ID), VALID_ORDER_AMOUNT, VALID_MEMBERSHIP_ID);
    }

    // ---------- Payment ----------
    public static CreatePaymentCommand validCreatePaymentCommand() {
        return new CreatePaymentCommand(VALID_ORDER_ID, VALID_TRANSACTION_ID, VALID_PAYMENT_DATE);
    }

    public static UpdatePaymentCommand updatePaymentCommand(Long paymentId) {
        return new UpdatePaymentCommand(paymentId, VALID_ORDER_ID, VALID_TRANSACTION_ID, VALID_PAYMENT_DATE);
    }
}
