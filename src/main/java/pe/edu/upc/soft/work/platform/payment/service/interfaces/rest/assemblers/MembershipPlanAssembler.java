package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateMembershipPlanCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateMembershipPlanCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.MembershipPlan;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.CreateMembershipPlanRequest;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.MembershipPlanResponse;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.UpdateMembershipPlanRequest;

public class MembershipPlanAssembler {

    public static CreateMembershipPlanCommand toCommandFromRequest(CreateMembershipPlanRequest request){
        return new CreateMembershipPlanCommand(request.planName(),request.price());
    }

    public static UpdateMembershipPlanCommand toCommandFromRequest(Long planId, UpdateMembershipPlanRequest request){
        return new UpdateMembershipPlanCommand(planId, request.planName(), request.price());
    }

    public static MembershipPlanResponse toResponseFromEntity(MembershipPlan membershipPlan){
        return new MembershipPlanResponse(membershipPlan.getId(), membershipPlan.getPlanName(), membershipPlan.getPrice());
    }
}
