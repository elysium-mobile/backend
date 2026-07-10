package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects.MembershipStatus;

import java.util.Date;

/**
 * Request object for creating a new Membership.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateMembershipRequest(
        @NotNull
        @NotBlank
        Long membershipPlanId,
        @NotNull
        @NotBlank
        Date membershipStart,
        @NotNull
        @NotBlank
        Date membershipOver,
        @NotNull
        @NotBlank
        String membershipStatus
) {}
