package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record CreateBenefitRequest(

        @NotBlank
        @JsonProperty("title")
        String title,
        @NotBlank
        @JsonProperty("description")
        String description,

        @NotBlank
        @JsonProperty("membershipPlanId")
        Long membershipPlanId

) {
}
