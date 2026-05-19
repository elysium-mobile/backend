package pe.edu.upc.soft.work.platform.payment.service.domain.services;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates.Membership;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetMembershipByIdQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetAllMembershipQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying Memberships in the system.
 */
public interface MembershipQueryService {

    /**
     * Retrieves a list of all Memberships in the system.
     */
    List<Membership> handle(GetAllMembershipQuery query);

    /**
     * Retrieves a Membership by their unique identifier.
     */
    Optional<Membership> handle(GetMembershipByIdQuery query);
}
