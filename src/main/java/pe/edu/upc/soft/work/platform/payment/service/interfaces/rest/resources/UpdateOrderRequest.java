package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderRequest(
        @NotNull
        @NotBlank
        @JsonProperty("userAccountId")
        Long userAccountId,
        Integer amount,
        @NotNull
        @NotBlank
        @JsonProperty("membershipId")
        Long membershipId
) {
}
