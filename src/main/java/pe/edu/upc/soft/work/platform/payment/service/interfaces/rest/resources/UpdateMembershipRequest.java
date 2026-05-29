package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects.MembershipStatus;

import java.util.Date;

/**
 * Request object for updating an existing Membership.
 */
public record UpdateMembershipRequest(
        @NotNull
        @NotBlank
        @JsonProperty("membershipStart")
        Date membershipStart,
        @NotNull
        @NotBlank
        @JsonProperty("membershipOver")
        Date membershipOver,
        @NotNull
        @NotBlank
        @JsonProperty("membershipStatus")
        String membershipStatus
) {}
