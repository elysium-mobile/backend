package pe.edu.upc.soft.work.platform.dashboard.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.WorkTeam;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetWorkTeamByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAllWorkTeamQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.WorkTeamQueryService;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.WorkTeamRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the WorkTeamQueryService interface.
 */
@Service
public class WorkTeamQueryServiceImpl implements WorkTeamQueryService {
    private final WorkTeamRepository workteamRepository;

    /**
     * Constructor for WorkTeamQueryServiceImpl.
     */
    public WorkTeamQueryServiceImpl(WorkTeamRepository workteamRepository) {
        this.workteamRepository = workteamRepository;
    }

    /**
     * Handles the GetAllWorkTeamQuery.
     */
    @Override
    public List<WorkTeam> handle(GetAllWorkTeamQuery query) {
        return workteamRepository.findAll();
    }

    /**
     * Handles the GetWorkTeamByIdQuery.
     */
    @Override
    public Optional<WorkTeam> handle(GetWorkTeamByIdQuery query) {
        return workteamRepository.findById(query.workteamId());
    }
}
