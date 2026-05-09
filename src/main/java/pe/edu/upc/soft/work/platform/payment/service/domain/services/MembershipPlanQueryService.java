package pe.edu.upc.soft.work.platform.payment.service.domain.services;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.MembershipPlan;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetMembershipPlanByIdQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetAllMembershipPlanQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying MembershipPlans in the system.
 */
public interface MembershipPlanQueryService {

    /**
     * Retrieves a list of all MembershipPlans in the system.
     */
    List<MembershipPlan> handle(GetAllMembershipPlanQuery query);

    /**
     * Retrieves a MembershipPlan by their unique identifier.
     */
    Optional<MembershipPlan> handle(GetMembershipPlanByIdQuery query);
}
