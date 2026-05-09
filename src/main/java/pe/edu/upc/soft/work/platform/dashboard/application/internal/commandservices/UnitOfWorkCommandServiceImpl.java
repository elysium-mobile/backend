package pe.edu.upc.soft.work.platform.dashboard.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.UnitOfWork;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateUnitOfWorkCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateUnitOfWorkCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.DeleteUnitOfWorkCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.UnitOfWorkCommandService;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.UnitOfWorkRepository;

import java.util.Optional;

@Service
public class UnitOfWorkCommandServiceImpl implements UnitOfWorkCommandService {
    private final UnitOfWorkRepository unitofworkRepository;

    public UnitOfWorkCommandServiceImpl(UnitOfWorkRepository unitofworkRepository) {
        this.unitofworkRepository = unitofworkRepository;
    }

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
}
