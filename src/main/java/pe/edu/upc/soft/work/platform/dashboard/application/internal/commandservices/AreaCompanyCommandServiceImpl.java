package pe.edu.upc.soft.work.platform.dashboard.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.AreaCompany;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateAreaCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateAreaCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.DeleteAreaCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.AreaCompanyCommandService;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.AreaCompanyRepository;

import java.util.Optional;

@Service
public class AreaCompanyCommandServiceImpl implements AreaCompanyCommandService {
    private final AreaCompanyRepository areacompanyRepository;

    public AreaCompanyCommandServiceImpl(AreaCompanyRepository areacompanyRepository) {
        this.areacompanyRepository = areacompanyRepository;
    }

    @Override
    public Long handle(CreateAreaCompanyCommand command) {
        var areacompany = new AreaCompany(command);
        try {
            areacompanyRepository.save(areacompany);
        } catch (Exception e) {
            throw new RuntimeException("Error creating AreaCompany: " + e.getMessage(), e);
        }
        return areacompany.getId();
    }

    @Override
    public Optional<AreaCompany> handle(UpdateAreaCompanyCommand command) {
        var areacompanyId = command.areacompanyId();
        if (!this.areacompanyRepository.existsById(areacompanyId)) {
            throw new RuntimeException("AreaCompany with ID " + areacompanyId + " does not exist.");
        }

        var areacompanyToUpdate = this.areacompanyRepository.findById(areacompanyId).get();
        areacompanyToUpdate.updateAreaCompany(command);
        try {
            var updatedAreaCompany = this.areacompanyRepository.save(areacompanyToUpdate);
            return Optional.of(updatedAreaCompany);
        } catch (Exception e) {
            throw new RuntimeException("Error updating AreaCompany: " + e.getMessage(), e);
        }
    }

    @Override
    public void handle(DeleteAreaCompanyCommand command) {
        if (!areacompanyRepository.existsById(command.areacompanyId())) {
            throw new RuntimeException("AreaCompany with ID " + command.areacompanyId() + " does not exist.");
        }
        try {
            areacompanyRepository.deleteById(command.areacompanyId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting AreaCompany: " + e.getMessage(), e);
        }
    }
}
