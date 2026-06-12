package pe.edu.upc.soft.work.platform.dashboard.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.AddWorkTeamToUnitOfWork;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.UnitOfWork;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateUnitOfWorkCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateUnitOfWorkCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.DeleteUnitOfWorkCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.UnitOfWorkCommandService;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.UnitOfWorkRepository;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.WorkTeamRepository;

import java.util.Optional;

/**
 * Service implementation for handling UnitOfWork commands.
 */
@Service
public class UnitOfWorkCommandServiceImpl implements UnitOfWorkCommandService {
    private final UnitOfWorkRepository unitofworkRepository;
    private final WorkTeamRepository workTeamRepository;

    /**
     * Constructor for UnitOfWorkCommandServiceImpl.
     * @param unitofworkRepository the repository for UnitOfWork persistence
     */
    public UnitOfWorkCommandServiceImpl(UnitOfWorkRepository unitofworkRepository,
                                        WorkTeamRepository workTeamRepository) {
        this.unitofworkRepository = unitofworkRepository;
        this.workTeamRepository = workTeamRepository;
    }

    /**
     * Handles the creation of a new UnitOfWork.
     * @param command the command to create a UnitOfWork
     * @return the generated ID of the new UnitOfWork
     */
    @Override
    public Long handle(CreateUnitOfWorkCommand command) {
        var unitofwork = new UnitOfWork(command);
        try {
            unitofworkRepository.save(unitofwork);
        } catch (Exception e) {
            throw new RuntimeException("Error creating UnitOfWork: " + e.getMessage(), e);
        }
        return unitofwork.getId();
    }

    /**
     * Handles the update of an existing UnitOfWork.
     * @param command the command to update a UnitOfWork
     * @return the updated UnitOfWork as an Optional
     */
    @Override
    public Optional<UnitOfWork> handle(UpdateUnitOfWorkCommand command) {
        var unitofworkId = command.unitofworkId();
        if (!this.unitofworkRepository.existsById(unitofworkId)) {
            throw new RuntimeException("UnitOfWork with ID " + unitofworkId + " does not exist.");
        }

        var unitofworkToUpdate = this.unitofworkRepository.findById(unitofworkId).get();
        unitofworkToUpdate.updateUnitOfWork(command);
        try {
            var updatedUnitOfWork = this.unitofworkRepository.save(unitofworkToUpdate);
            return Optional.of(updatedUnitOfWork);
        } catch (Exception e) {
            throw new RuntimeException("Error updating UnitOfWork: " + e.getMessage(), e);
        }
    }

    /**
     * Handles the deletion of a UnitOfWork.
     * @param command the command to delete a UnitOfWork
     */
    @Override
    public void handle(DeleteUnitOfWorkCommand command) {
        if (!unitofworkRepository.existsById(command.unitofworkId())) {
            throw new RuntimeException("UnitOfWork with ID " + command.unitofworkId() + " does not exist.");
        }
        try {
            unitofworkRepository.deleteById(command.unitofworkId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting UnitOfWork: " + e.getMessage(), e);
        }
    }

    @Override
    public void handle(AddWorkTeamToUnitOfWork command) {
        var workTeam = workTeamRepository.findById(command.widgetId())
                .orElseThrow(() -> new RuntimeException(
                        "WorkTeam with ID " + command.widgetId() + " does not exist."));

        var unitOfWork = unitofworkRepository.findById(command.unitOfWorkId())
                .orElseThrow(() -> new RuntimeException(
                        "UnitOfWork with ID " + command.unitOfWorkId() + " does not exist."));

        try {
            unitOfWork.addWorkTeam(workTeam);
            unitofworkRepository.save(unitOfWork);
        } catch (IllegalStateException ex) {
            throw new IllegalArgumentException("Domain error while adding WorkTeam: " + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error adding WorkTeam to UnitOfWork: " + ex.getMessage(), ex);
        }
    }
}