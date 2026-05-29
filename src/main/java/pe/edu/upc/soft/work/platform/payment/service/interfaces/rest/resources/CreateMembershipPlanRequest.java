package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record CreateMembershipPlanRequest(
        @NotBlank
        @JsonProperty("planName")
        String planName
) {
}
