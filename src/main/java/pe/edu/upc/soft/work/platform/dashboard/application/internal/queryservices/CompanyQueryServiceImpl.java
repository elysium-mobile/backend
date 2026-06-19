package pe.edu.upc.soft.work.platform.dashboard.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates.Company;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetCompaniesByNameQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetCompanyByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAllCompanyQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.CompanyQueryService;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.CompanyRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the CompanyQueryService interface.
 */
@Service
public class CompanyQueryServiceImpl implements CompanyQueryService {
    private final CompanyRepository companyRepository;

    /**
     * Constructor for CompanyQueryServiceImpl.
     */
    public CompanyQueryServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    /**
     * Handles the GetAllCompanyQuery.
     */
    @Override
    public List<Company> handle(GetAllCompanyQuery query) {
        return companyRepository.findAll();
    }

    /**
     * Handles the GetCompanyByIdQuery.
     */
    @Override
    public Optional<Company> handle(GetCompanyByIdQuery query) {
        return companyRepository.findById(query.companyId());
    }

    /**
     *  Handles the GetCompaniesByNameQuery.
     * @param query the query containing the name to search for
     * @return  a list of companies whose names contain the specified name, ignoring case
     */
    @Override
    public List<Company> handle(GetCompaniesByNameQuery query) {
        return companyRepository.findByNameContainingIgnoreCase(query.name());
    }
}
