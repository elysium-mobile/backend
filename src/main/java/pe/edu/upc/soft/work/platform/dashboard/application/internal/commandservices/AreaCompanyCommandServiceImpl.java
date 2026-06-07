package pe.edu.upc.soft.work.platform.dashboard.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.dashboard.application.internal.outboundservices.acl.ExternalIamServiceFromDashboard;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.AreaCompany;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateAreaCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateAreaCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.DeleteAreaCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.AreaCompanyCommandService;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.AreaCompanyRepository;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.CompanyRepository;

import java.util.Optional;

/**
 * Service implementation for handling AreaCompany commands.
 */
@Service
public class AreaCompanyCommandServiceImpl implements AreaCompanyCommandService {
    private final AreaCompanyRepository areacompanyRepository;
    private final CompanyRepository companyRepository;

    private final ExternalIamServiceFromDashboard externalIamServiceFromDashboard;

    /**
     * Constructor for AreaCompanyCommandServiceImpl.
     * @param areacompanyRepository the repository for AreaCompany persistence
     */
    public AreaCompanyCommandServiceImpl(AreaCompanyRepository areacompanyRepository,
                                         ExternalIamServiceFromDashboard externalIamServiceFromDashboard,
                                         CompanyRepository companyRepository) {
        this.areacompanyRepository = areacompanyRepository;
        this.externalIamServiceFromDashboard = externalIamServiceFromDashboard;
        this.companyRepository = companyRepository;
    }

    /**
     * Handles the creation of an AreaCompany.
     * @param command the command to create an AreaCompany
     * @return the generated ID of the new AreaCompany
     */
    @Override
    public Long handle(CreateAreaCompanyCommand command) {
        if (!companyRepository.existsById(command.companyId())) {
            throw new RuntimeException("Company with ID " + command.companyId() + " does not exist.");
        }
        var areacompany = new AreaCompany(command);
        try {
            areacompanyRepository.save(areacompany);
        } catch (Exception e) {
            throw new RuntimeException("Error creating AreaCompany: " + e.getMessage(), e);
        }
        return areacompany.getId();
    }

    /**
     * Handles the update of an existing AreaCompany.
     * @param command the command to update an AreaCompany
     * @return the updated AreaCompany as an Optional
     */
    @Override
    public Optional<AreaCompany> handle(UpdateAreaCompanyCommand command) {
        var areacompanyId = command.areaCompanyId();
        if (!this.areacompanyRepository.existsById(areacompanyId)) {
            throw new RuntimeException("AreaCompany with ID " + areacompanyId + " does not exist.");
        }
        if (!companyRepository.existsById(command.companyId())) {
            throw new RuntimeException("Company with ID " + command.companyId() + " does not exist.");
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

    /**
     * Handles the deletion of an AreaCompany.
     * @param command the command to delete an AreaCompany
     */
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