package pe.edu.upc.soft.work.platform.payment.service.domain.services;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates.Membership;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateMembershipCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateMembershipCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.DeleteMembershipCommand;

import java.util.Optional;

/**
 * Service interface for handling Membership-related commands.
 */
public interface MembershipCommandService {

    /**
     * Handles the creation of a new Membership.
     */
    Long handle(CreateMembershipCommand command);

    /**
     * Handles the update of an existing Membership.
     */
    Optional<Membership> handle(UpdateMembershipCommand command);

    /**
     * Handles the deletion of an existing Membership.
     */
    void handle(DeleteMembershipCommand command);
}
