package pe.edu.upc.soft.work.platform.payment.service.domain.services;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.Benefit;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetBenefitByIdQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetAllBenefitQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying Benefits in the system.
 */
public interface BenefitQueryService {

    /**
     * Retrieves a list of all Benefits in the system.
     */
    List<Benefit> handle(GetAllBenefitQuery query);

    /**
     * Retrieves a Benefit by their unique identifier.
     */
    Optional<Benefit> handle(GetBenefitByIdQuery query);
}
