package pe.edu.upc.soft.work.platform.dashboard.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates.Dashboard;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetDashboardByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAllDashboardQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.DashboardQueryService;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.DashboardRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the DashboardQueryService interface.
 */
@Service
public class DashboardQueryServiceImpl implements DashboardQueryService {
    private final DashboardRepository dashboardRepository;

    /**
     * Constructor for DashboardQueryServiceImpl.
     */
    public DashboardQueryServiceImpl(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    /**
     * Handles the GetAllDashboardQuery.
     */
    @Override
    public List<Dashboard> handle(GetAllDashboardQuery query) {
        return dashboardRepository.findAll();
    }

    /**
     * Handles the GetDashboardByIdQuery.
     */
    @Override
    public Optional<Dashboard> handle(GetDashboardByIdQuery query) {
        return dashboardRepository.findById(query.dashboardId());
    }
}
