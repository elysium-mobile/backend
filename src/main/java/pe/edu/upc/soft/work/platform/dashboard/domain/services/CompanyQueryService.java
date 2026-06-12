package pe.edu.upc.soft.work.platform.dashboard.domain.services;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates.Company;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetCompanyByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAllCompanyQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetCompaniesByNameQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying Companys in the system.
 */
public interface CompanyQueryService {

    /**
     * Retrieves a list of all Companys in the system.
     */
    List<Company> handle(GetAllCompanyQuery query);

    /**
     * Retrieves a Company by their unique identifier.
     */
    Optional<Company> handle(GetCompanyByIdQuery query);


    List<Company> handle(GetCompaniesByNameQuery query);
}
