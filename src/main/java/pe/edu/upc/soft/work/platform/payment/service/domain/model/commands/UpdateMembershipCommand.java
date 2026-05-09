package pe.edu.upc.soft.work.platform.payment.service.domain.model.commands;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects.MembershipStatus;

import java.util.Objects;
import java.util.Date;

/**
 * Command to update an existing Membership
 */
public record UpdateMembershipCommand(Long membershipId, Date membershipStart, Date membershipOver, MembershipStatus membershipStatus) {

    /**
     * Constructor with validation
     */
    public UpdateMembershipCommand {
        Objects.requireNonNull(membershipId, "[UpdateMembershipCommand] membershipId must not be null");
    }
}
