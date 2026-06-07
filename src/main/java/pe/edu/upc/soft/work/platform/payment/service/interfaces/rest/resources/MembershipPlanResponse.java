package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

import java.util.List;

public record MembershipPlanResponse(
        Long planId,
        String planName,
        Integer price,
        List<BenefitResponse> benefitResponseList
) {
}
