package pe.edu.upc.soft.work.platform.payment.service.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.Benefit;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetBenefitByIdQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetAllBenefitQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.BenefitQueryService;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.BenefitRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the BenefitQueryService interface.
 */
@Service
public class BenefitQueryServiceImpl implements BenefitQueryService {
    private final BenefitRepository benefitRepository;

    /**
     * Constructor for BenefitQueryServiceImpl.
     */
    public BenefitQueryServiceImpl(BenefitRepository benefitRepository) {
        this.benefitRepository = benefitRepository;
    }

    /**
     * Handles the GetAllBenefitQuery.
     */
    @Override
    public List<Benefit> handle(GetAllBenefitQuery query) {
        return benefitRepository.findAll();
    }

    /**
     * Handles the GetBenefitByIdQuery.
     */
    @Override
    public Optional<Benefit> handle(GetBenefitByIdQuery query) {
        return benefitRepository.findById(query.benefitId());
    }
}
