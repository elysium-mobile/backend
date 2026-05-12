package pe.edu.upc.soft.work.platform.dashboard.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.WorkTeam;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateWorkTeamCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateWorkTeamCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.DeleteWorkTeamCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.WorkTeamCommandService;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.WorkTeamRepository;

import java.util.Optional;

/**
 * Service implementation for handling WorkTeam commands.
 */
@Service
public class WorkTeamCommandServiceImpl implements WorkTeamCommandService {
    private final WorkTeamRepository workteamRepository;

    /**
     * Constructor for WorkTeamCommandServiceImpl.
     * @param workteamRepository the repository for WorkTeam persistence
     */
    public WorkTeamCommandServiceImpl(WorkTeamRepository workteamRepository) {
        this.workteamRepository = workteamRepository;
    }

    /**
     * Handles the creation of a new WorkTeam.
     * @param command the command to create a WorkTeam
     * @return the generated ID of the new WorkTeam
     */
    @Override
    public Long handle(CreateWorkTeamCommand command) {
        var workteam = new WorkTeam(command);
        try {
            workteamRepository.save(workteam);
        } catch (Exception e) {
            throw new RuntimeException("Error creating WorkTeam: " + e.getMessage(), e);
        }
        return workteam.getId();
    }

    /**
     * Handles the update of an existing WorkTeam.
     * @param command the command to update a WorkTeam
     * @return the updated WorkTeam as an Optional
     */
    @Override
    public Optional<WorkTeam> handle(UpdateWorkTeamCommand command) {
        var workteamId = command.workteamId();
        if (!this.workteamRepository.existsById(workteamId)) {
            throw new RuntimeException("WorkTeam with ID " + workteamId + " does not exist.");
        }

        var workteamToUpdate = this.workteamRepository.findById(workteamId).get();
        workteamToUpdate.updateWorkTeam(command);
        try {
            var updatedWorkTeam = this.workteamRepository.save(workteamToUpdate);
            return Optional.of(updatedWorkTeam);
        } catch (Exception e) {
            throw new RuntimeException("Error updating WorkTeam: " + e.getMessage(), e);
        }
    }

    /**
     * Handles the deletion of a WorkTeam.
     * @param command the command to delete a WorkTeam
     */
    @Override
    public void handle(DeleteWorkTeamCommand command) {
        if (!workteamRepository.existsById(command.workteamId())) {
            throw new RuntimeException("WorkTeam with ID " + command.workteamId() + " does not exist.");
        }
        try {
            workteamRepository.deleteById(command.workteamId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting WorkTeam: " + e.getMessage(), e);
        }
    }
}