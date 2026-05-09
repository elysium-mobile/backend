package pe.edu.upc.soft.work.platform.dashboard.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.AreaCompany;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAreaCompanyByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAllAreaCompanyQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.AreaCompanyQueryService;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.AreaCompanyRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the AreaCompanyQueryService interface.
 */
@Service
public class AreaCompanyQueryServiceImpl implements AreaCompanyQueryService {
    private final AreaCompanyRepository areacompanyRepository;

    /**
     * Constructor for AreaCompanyQueryServiceImpl.
     */
    public AreaCompanyQueryServiceImpl(AreaCompanyRepository areacompanyRepository) {
        this.areacompanyRepository = areacompanyRepository;
    }

    /**
     * Handles the GetAllAreaCompanyQuery.
     */
    @Override
    public List<AreaCompany> handle(GetAllAreaCompanyQuery query) {
        return areacompanyRepository.findAll();
    }

    /**
     * Handles the GetAreaCompanyByIdQuery.
     */
    @Override
    public Optional<AreaCompany> handle(GetAreaCompanyByIdQuery query) {
        return areacompanyRepository.findById(query.areacompanyId());
    }
}
