package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates.Membership;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateMembershipCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateMembershipCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects.MembershipStatus;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.CreateMembershipRequest;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.UpdateMembershipRequest;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.MembershipResponse;

public class MembershipAssembler {

    /**
     * Converts a CreateMembershipRequest to a CreateMembershipCommand.
     */
    public static CreateMembershipCommand toCommandFromRequest(CreateMembershipRequest request) {
        return new CreateMembershipCommand(request.membershipStart(), request.membershipOver(), MembershipStatus.valueOf(request.membershipStatus()));
    }

    /**
     * Converts an UpdateMembershipRequest to an UpdateMembershipCommand.
     */
    public static UpdateMembershipCommand toCommandFromRequest(Long membershipId, UpdateMembershipRequest request) {
        return new UpdateMembershipCommand(membershipId, request.membershipStart(), request.membershipOver(), MembershipStatus.valueOf(request.membershipStatus()));
    }

    /**
     * Converts a Membership entity to a MembershipResponse.
     */
    public static MembershipResponse toResponseFromEntity(Membership membership) {
        return new MembershipResponse(membership.getId(), membership.getMembershipStart(), membership.getMembershipOver(), membership.getMembershipStatus().name());
    }
}
