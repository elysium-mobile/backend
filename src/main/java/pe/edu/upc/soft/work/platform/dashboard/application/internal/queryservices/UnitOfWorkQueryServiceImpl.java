package pe.edu.upc.soft.work.platform.dashboard.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.UnitOfWork;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetUnitOfWorkByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAllUnitOfWorkQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.UnitOfWorkQueryService;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.UnitOfWorkRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the UnitOfWorkQueryService interface.
 */
@Service
public class UnitOfWorkQueryServiceImpl implements UnitOfWorkQueryService {
    private final UnitOfWorkRepository unitofworkRepository;

    /**
     * Constructor for UnitOfWorkQueryServiceImpl.
     */
    public UnitOfWorkQueryServiceImpl(UnitOfWorkRepository unitofworkRepository) {
        this.unitofworkRepository = unitofworkRepository;
    }

    /**
     * Handles the GetAllUnitOfWorkQuery.
     */
    @Override
    public List<UnitOfWork> handle(GetAllUnitOfWorkQuery query) {
        return unitofworkRepository.findAll();
    }

    /**
     * Handles the GetUnitOfWorkByIdQuery.
     */
    @Override
    public Optional<UnitOfWork> handle(GetUnitOfWorkByIdQuery query) {
        return unitofworkRepository.findById(query.unitofworkId());
    }
}
