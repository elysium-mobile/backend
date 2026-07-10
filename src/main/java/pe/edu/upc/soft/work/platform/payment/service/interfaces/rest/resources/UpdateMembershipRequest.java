package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects.MembershipStatus;

import java.util.Date;

/**
 * Request object for updating an existing Membership.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UpdateMembershipRequest(

        @NotNull
        Long membershipPlanId,
        @NotNull
        Date membershipStart,
        @NotNull
        Date membershipOver,
        @NotNull
        @NotBlank
        String membershipStatus
) {}
