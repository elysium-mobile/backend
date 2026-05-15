package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

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
        Date membershipStart,
        @NotNull
        @NotBlank
        Date membershipOver,
        @NotNull
        @NotBlank
        String membershipStatus
) {}
