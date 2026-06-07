package pe.edu.upc.soft.work.platform.payment.service.application.internal.eventhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.events.MembershipActivatedEvent;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetMembershipByIdQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.MembershipQueryService;

/**
 * Event handler responsible for reacting to a MembershipActivatedEvent.
 */
@Service
public class MembershipActivatedEventHandler {

    private final MembershipQueryService membershipQueryService;
    private static final Logger LOGGER = LoggerFactory.getLogger(MembershipActivatedEventHandler.class);

    /**
     * Constructor for MembershipActivatedEventHandler.
     * @param membershipQueryService service to query the Membership aggregate
     */
    public MembershipActivatedEventHandler(MembershipQueryService membershipQueryService) {
        this.membershipQueryService = membershipQueryService;
    }

    /**
     * Handles the MembershipActivatedEvent after a membership status has changed.
     * @param event the MembershipActivatedEvent containing membership ID and new status
     */
    @EventListener
    public void on(MembershipActivatedEvent event) {
        var getMembershipByIdQuery = new GetMembershipByIdQuery(event.getMembershipId());
        var membership = membershipQueryService.handle(getMembershipByIdQuery);

        if (membership.isPresent()) {
            LOGGER.info("Membership with ID: {} successfully updated to status: {}",
                    event.getMembershipId(), event.getMembershipStatus());
        } else {
            LOGGER.warn("Error: Membership with ID {} not found after status change.", event.getMembershipId());
        }
    }
}
