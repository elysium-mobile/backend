package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record AddBenefitToMembershipPlanRequest(

        @NotNull
        @JsonProperty("benefitId")
        Long benefitId
) {
}
