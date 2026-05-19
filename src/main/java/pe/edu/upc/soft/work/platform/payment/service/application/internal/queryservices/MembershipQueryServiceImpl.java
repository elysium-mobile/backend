package pe.edu.upc.soft.work.platform.payment.service.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates.Membership;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetMembershipByIdQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetAllMembershipQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.MembershipQueryService;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.MembershipRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the MembershipQueryService interface.
 */
@Service
public class MembershipQueryServiceImpl implements MembershipQueryService {
    private final MembershipRepository membershipRepository;

    /**
     * Constructor for MembershipQueryServiceImpl.
     */
    public MembershipQueryServiceImpl(MembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }

    /**
     * Handles the GetAllMembershipQuery.
     */
    @Override
    public List<Membership> handle(GetAllMembershipQuery query) {
        return membershipRepository.findAll();
    }

    /**
     * Handles the GetMembershipByIdQuery.
     */
    @Override
    public Optional<Membership> handle(GetMembershipByIdQuery query) {
        return membershipRepository.findById(query.membershipId());
    }
}
