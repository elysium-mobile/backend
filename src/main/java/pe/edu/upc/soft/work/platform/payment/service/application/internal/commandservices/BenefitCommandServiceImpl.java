package pe.edu.upc.soft.work.platform.payment.service.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.Benefit;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateBenefitCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateBenefitCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.DeleteBenefitCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.BenefitCommandService;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.BenefitRepository;

import java.util.Optional;

/**
 * Service implementation for handling Benefit commands.
 */
@Service
public class BenefitCommandServiceImpl implements BenefitCommandService {
    private final BenefitRepository benefitRepository;

    /**
     * Constructor for BenefitCommandServiceImpl
     * @param benefitRepository the repository for Benefit persistence
     */
    public BenefitCommandServiceImpl(BenefitRepository benefitRepository) {
        this.benefitRepository = benefitRepository;
    }

    /**
     * Handles the creation of an Benefit
     * @param command the command to create a Benefit
     * @return the generated ID of the new Benefit
     */
    @Override
    public Long handle(CreateBenefitCommand command) {
        var benefit = new Benefit(command);
        try {
            benefitRepository.save(benefit);
        } catch (Exception e) {
            throw new RuntimeException("Error creating Benefit: " + e.getMessage(), e);
        }
        return benefit.getId();
    }

    /**
     * Handles the update of an existing Benefit
     * @param command the command to update an Benefit
     * @return the updated Benefit as an Optional
     */
    @Override
    public Optional<Benefit> handle(UpdateBenefitCommand command) {
        var benefitId = command.benefitId();
        if (!this.benefitRepository.existsById(benefitId)) {
            throw new RuntimeException("Benefit with ID " + benefitId + " does not exist.");
        }

        var benefitToUpdate = this.benefitRepository.findById(benefitId).get();
        benefitToUpdate.updateBenefit(command);
        try {
            var updatedBenefit = this.benefitRepository.save(benefitToUpdate);
            return Optional.of(updatedBenefit);
        } catch (Exception e) {
            throw new RuntimeException("Error updating Benefit: " + e.getMessage(), e);
        }
    }

    /**
     * Handle the deletion of an Benefit
     * @param command the command to delete an Benefit
     */
    @Override
    public void handle(DeleteBenefitCommand command) {
        if (!benefitRepository.existsById(command.benefitId())) {
            throw new RuntimeException("Benefit with ID " + command.benefitId() + " does not exist.");
        }
        try {
            benefitRepository.deleteById(command.benefitId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting Benefit: " + e.getMessage(), e);
        }
    }
}
