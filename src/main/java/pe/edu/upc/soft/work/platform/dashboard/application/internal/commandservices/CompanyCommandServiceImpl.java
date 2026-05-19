package pe.edu.upc.soft.work.platform.dashboard.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates.Company;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.DeleteCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.CompanyCommandService;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.CompanyRepository;

import java.util.Optional;

/**
 * Service implementation for handling Company commands.
 */
@Service
public class CompanyCommandServiceImpl implements CompanyCommandService {
    private final CompanyRepository companyRepository;

    /**
     * Constructor for CompanyCommandServiceImpl.
     * @param companyRepository the repository for Company persistence
     */
    public CompanyCommandServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    /**
     * Handles the creation of a new Company.
     * @param command the command to create a Company
     * @return the generated ID of the new Company
     */
    @Override
    public Long handle(CreateCompanyCommand command) {
        var company = new Company(command);
        try {
            companyRepository.save(company);
        } catch (Exception e) {
            throw new RuntimeException("Error creating Company: " + e.getMessage(), e);
        }
        return company.getId();
    }

    /**
     * Handles the update of an existing Company.
     * @param command the command to update a Company
     * @return the updated Company as an Optional
     */
    @Override
    public Optional<Company> handle(UpdateCompanyCommand command) {
        var companyId = command.companyId();
        if (!this.companyRepository.existsById(companyId)) {
            throw new RuntimeException("Company with ID " + companyId + " does not exist.");
        }

        var companyToUpdate = this.companyRepository.findById(companyId).get();
        companyToUpdate.updateCompany(command);
        try {
            var updatedCompany = this.companyRepository.save(companyToUpdate);
            return Optional.of(updatedCompany);
        } catch (Exception e) {
            throw new RuntimeException("Error updating Company: " + e.getMessage(), e);
        }
    }

    /**
     * Handles the deletion of a Company.
     * @param command the command to delete a Company
     */
    @Override
    public void handle(DeleteCompanyCommand command) {
        if (!companyRepository.existsById(command.companyId())) {
            throw new RuntimeException("Company with ID " + command.companyId() + " does not exist.");
        }
        try {
            companyRepository.deleteById(command.companyId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting Company: " + e.getMessage(), e);
        }
    }
}