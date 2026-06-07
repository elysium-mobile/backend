package pe.edu.upc.soft.work.platform.payment.service.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects.MembershipStatus;

/**
 * MembershipActivatedEvent
 * Event triggered when a Membership changes its status.
 */
@Getter
public class MembershipActivatedEvent extends ApplicationEvent {
    /** The ID of the membership whose status changed. */
    private final Long membershipId;
    /** The new status of the membership. */
    private final MembershipStatus membershipStatus;

    /**
     * MembershipActivatedEvent Constructor
     * @param source           the source of the event
     * @param membershipId     the ID of the membership
     * @param membershipStatus the new membership status
     */
    public MembershipActivatedEvent(Object source, Long membershipId, MembershipStatus membershipStatus) {
        super(source);
        this.membershipId = membershipId;
        this.membershipStatus = membershipStatus;
    }
}
