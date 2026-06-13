package pe.edu.upc.soft.work.platform.dashboard.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.dashboard.application.internal.outboundservices.acl.ExternalIamServiceFromDashboard;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.AddUnitOfWorkToAreaCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.AreaCompany;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateAreaCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateAreaCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.DeleteAreaCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.AreaCompanyCommandService;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.AreaCompanyRepository;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.CompanyRepository;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.UnitOfWorkRepository;

import java.util.Optional;

/**
 * Service implementation for handling AreaCompany commands.
 */
@Service
public class AreaCompanyCommandServiceImpl implements AreaCompanyCommandService {
    private final AreaCompanyRepository areacompanyRepository;
    private final CompanyRepository companyRepository;
    private final UnitOfWorkRepository unitOfWorkRepository;

    private final ExternalIamServiceFromDashboard externalIamServiceFromDashboard;

    /**
     * Constructor for AreaCompanyCommandServiceImpl.
     * @param areacompanyRepository the repository for AreaCompany persistence
     */
    public AreaCompanyCommandServiceImpl(AreaCompanyRepository areacompanyRepository,
                                         ExternalIamServiceFromDashboard externalIamServiceFromDashboard,
                                         CompanyRepository companyRepository,
                                         UnitOfWorkRepository unitOfWorkRepository) {
        this.areacompanyRepository = areacompanyRepository;
        this.externalIamServiceFromDashboard = externalIamServiceFromDashboard;
        this.companyRepository = companyRepository;
        this.unitOfWorkRepository = unitOfWorkRepository;
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
        var areaCompany = areacompanyRepository.findById(command.areacompanyId())
            .orElseThrow(() -> new RuntimeException("AreaCompany with ID " + command.areacompanyId() + " does not exist."));
        var company = companyRepository.findById(areaCompany.getCompanyId())
            .orElseThrow(() -> new RuntimeException(
                "[AreaCompanyCommandServiceImpl] Company with ID " + areaCompany.getCompanyId() + " not found for AreaCompany " + command.areacompanyId()));
        try {
            company.removeAreaCompany(command.areacompanyId());
            companyRepository.save(company);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting AreaCompany: " + e.getMessage(), e);
        }
    }

    /**
     *  Handles the addition of a UnitOfWork to an AreaCompany.
     * @param command   the command to add a UnitOfWork to an AreaCompany
     */
    @Override
    public void handle(AddUnitOfWorkToAreaCompanyCommand command) {
        var unitOfWork = unitOfWorkRepository.findById(command.unitOfWork())
                .orElseThrow(()-> new RuntimeException("UnitOfWork with ID " + command.unitOfWork() + " does not exist."));
        var areaCompany = areacompanyRepository.findById(command.areaCompanyId())
                .orElseThrow(()-> new RuntimeException("AreaCompany with ID " + command.areaCompanyId() + " does not exist."));
        try {
            areaCompany.addUnitOfWork(unitOfWork);
            areacompanyRepository.save(areaCompany);
        }catch (IllegalStateException ex){

        }catch (Exception ex){
            throw new RuntimeException("Error adding UnitOfWork to AreaCompany: " + ex.getMessage(), ex);
        }
    }
}