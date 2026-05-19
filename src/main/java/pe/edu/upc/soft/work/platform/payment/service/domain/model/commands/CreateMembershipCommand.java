package pe.edu.upc.soft.work.platform.payment.service.domain.model.commands;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects.MembershipStatus;

import java.util.Objects;
import java.util.Date;

/**
 * Command to create a new Membership
 */
public record CreateMembershipCommand(Date membershipStart, Date membershipOver, MembershipStatus membershipStatus) {

    /**
     * Constructor with validation
     */
    public CreateMembershipCommand {
        Objects.requireNonNull(membershipStart, "[CreateMembershipCommand] membershipStart must not be null");
        Objects.requireNonNull(membershipOver, "[CreateMembershipCommand] membershipOver must not be null");
        Objects.requireNonNull(membershipStatus, "[CreateMembershipCommand] membershipStatus must not be null");
    }
}
