package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects.MembershipStatus;

import java.util.Date;

/**
 * Response object representing a Membership in the system.
 */
public record MembershipResponse(
        Long membershipId,
        Date membershipStart,
        Date membershipOver,
        MembershipStatus membershipStatus
) {}
