package pe.edu.upc.soft.work.platform.payment.service.domain.services;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.MembershipPlan;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateMembershipPlanCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateMembershipPlanCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.DeleteMembershipPlanCommand;

import java.util.Optional;

/**
 * Service interface for handling MembershipPlan-related commands.
 */
public interface MembershipPlanCommandService {

    /**
     * Handles the creation of a new MembershipPlan.
     */
    Long handle(CreateMembershipPlanCommand command);

    /**
     * Handles the update of an existing MembershipPlan.
     */
    Optional<MembershipPlan> handle(UpdateMembershipPlanCommand command);

    /**
     * Handles the deletion of an existing MembershipPlan.
     */
    void handle(DeleteMembershipPlanCommand command);
}
