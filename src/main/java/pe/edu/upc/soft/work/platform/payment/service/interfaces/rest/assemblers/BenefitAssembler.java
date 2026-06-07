package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateBenefitCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateBenefitCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.Benefit;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.BenefitResponse;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.CreateBenefitRequest;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.UpdateBenefitRequest;

public class BenefitAssembler {


    public static CreateBenefitCommand toCommandFromRequest(CreateBenefitRequest request){
        return new CreateBenefitCommand(request.title(),request.description(), request.membershipPlanId());
    }

    public static UpdateBenefitCommand toCommandFromRequest(Long benefitId, UpdateBenefitRequest request)
    {
        return new UpdateBenefitCommand(benefitId,request.title(),request.description(), request.membershipPlanId());
    }

    public static BenefitResponse toResponseFromEntity(Benefit benefit){
        return new BenefitResponse(benefit.getId(), benefit.getTitle(), benefit.getDescription(), benefit.getMembershipPlanId());
    }
}
