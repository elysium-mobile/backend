package pe.edu.upc.soft.work.platform.profile.performance.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.aggregates.Performance;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.queries.GetPerformanceByIdQuery;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.queries.GetAllPerformanceQuery;
import pe.edu.upc.soft.work.platform.profile.performance.domain.services.PerformanceQueryService;
import pe.edu.upc.soft.work.platform.profile.performance.infrastructure.persistence.jpa.repositories.PerformanceRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the PerformanceQueryService interface.
 */
@Service
public class PerformanceQueryServiceImpl implements PerformanceQueryService {
    private final PerformanceRepository performanceRepository;

    /**
     * Constructor for PerformanceQueryServiceImpl.
     */
    public PerformanceQueryServiceImpl(PerformanceRepository performanceRepository) {
        this.performanceRepository = performanceRepository;
    }

    /**
     * Handles the GetAllPerformanceQuery.
     */
    @Override
    public List<Performance> handle(GetAllPerformanceQuery query) {
        return performanceRepository.findAll();
    }

    /**
     * Handles the GetPerformanceByIdQuery.
     */
    @Override
    public Optional<Performance> handle(GetPerformanceByIdQuery query) {
        return performanceRepository.findById(query.performanceId());
    }
}
