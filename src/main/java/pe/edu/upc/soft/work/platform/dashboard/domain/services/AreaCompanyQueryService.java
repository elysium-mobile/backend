package pe.edu.upc.soft.work.platform.dashboard.domain.services;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.AreaCompany;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAreaCompanyByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAllAreaCompanyQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying AreaCompanys in the system.
 */
public interface AreaCompanyQueryService {

    /**
     * Retrieves a list of all AreaCompanys in the system.
     */
    List<AreaCompany> handle(GetAllAreaCompanyQuery query);

    /**
     * Retrieves a AreaCompany by their unique identifier.
     */
    Optional<AreaCompany> handle(GetAreaCompanyByIdQuery query);
}
