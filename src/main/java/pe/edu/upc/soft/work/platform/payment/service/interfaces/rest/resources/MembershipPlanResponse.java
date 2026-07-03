package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MembershipPlanResponse(
        Long planId,
        String planName,
        Integer price,
        Long membershipId,
        List<BenefitResponse> benefitResponseList
) {
}
