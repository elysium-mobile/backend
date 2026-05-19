package pe.edu.upc.soft.work.platform.payment.service.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.MembershipPlan;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetMembershipPlanByIdQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetAllMembershipPlanQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.MembershipPlanQueryService;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.MembershipPlanRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the MembershipPlanQueryService interface.
 */
@Service
public class MembershipPlanQueryServiceImpl implements MembershipPlanQueryService {
    private final MembershipPlanRepository membershipplanRepository;

    /**
     * Constructor for MembershipPlanQueryServiceImpl.
     */
    public MembershipPlanQueryServiceImpl(MembershipPlanRepository membershipplanRepository) {
        this.membershipplanRepository = membershipplanRepository;
    }

    /**
     * Handles the GetAllMembershipPlanQuery.
     */
    @Override
    public List<MembershipPlan> handle(GetAllMembershipPlanQuery query) {
        return membershipplanRepository.findAll();
    }

    /**
     * Handles the GetMembershipPlanByIdQuery.
     */
    @Override
    public Optional<MembershipPlan> handle(GetMembershipPlanByIdQuery query) {
        return membershipplanRepository.findById(query.membershipplanId());
    }
}
