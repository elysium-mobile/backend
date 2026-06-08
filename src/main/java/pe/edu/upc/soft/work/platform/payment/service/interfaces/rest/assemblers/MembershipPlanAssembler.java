package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.AddBenefitToMembershipPlan;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateMembershipPlanCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateMembershipPlanCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.MembershipPlan;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.*;

import java.util.ArrayList;
import java.util.List;

public class MembershipPlanAssembler {

    public static CreateMembershipPlanCommand toCommandFromRequest(CreateMembershipPlanRequest request){
        return new CreateMembershipPlanCommand(request.planName(),request.price(), new ArrayList<>(), request.membershipId());
    }

    public static UpdateMembershipPlanCommand toCommandFromRequest(Long planId, UpdateMembershipPlanRequest request){
        return new UpdateMembershipPlanCommand(planId, request.planName(), request.price(), request.membershipId());
    }

    public static AddBenefitToMembershipPlan toCommandFromRequest(Long membershipPlanId, AddBenefitToMembershipPlanRequest request){
        return new AddBenefitToMembershipPlan(request.benefitId(), membershipPlanId);
    }

    public static MembershipPlanResponse toResponseFromEntity(MembershipPlan membershipPlan){

        List<BenefitResponse> benefitResponseList = membershipPlan.getBenefits().stream()
                .map(benefit -> new BenefitResponse(benefit.getId(),benefit.getTitle(), benefit.getDescription(), benefit.getMembershipPlanId()))
                .toList();
        return new MembershipPlanResponse(membershipPlan.getId(), membershipPlan.getPlanName(), membershipPlan.getPrice(),membershipPlan.getMembershipId(), benefitResponseList);
    }
}
