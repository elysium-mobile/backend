package pe.edu.upc.soft.work.platform.payment.service.application.internal.commandservices;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.Benefit;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateBenefitCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateBenefitCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.DeleteBenefitCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.BenefitCommandService;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.BenefitRepository;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.MembershipPlanRepository;

import java.util.Optional;

/**
 * Service implementation for handling Benefit commands.
 */
@Service
@Transactional
public class BenefitCommandServiceImpl implements BenefitCommandService {
    private final BenefitRepository benefitRepository;
    private final MembershipPlanRepository membershipPlanRepository;

    /**
     * Constructor for BenefitCommandServiceImpl
     * @param benefitRepository the repository for Benefit persistence
     */
    public BenefitCommandServiceImpl(BenefitRepository benefitRepository,
                                     MembershipPlanRepository membershipPlanRepository) {
        this.benefitRepository = benefitRepository;
        this.membershipPlanRepository = membershipPlanRepository;
    }

    /**
     * Handles the creation of an Benefit
     * @param command the command to create a Benefit
     * @return the generated ID of the new Benefit
     */
    @Override
    public Long handle(CreateBenefitCommand command) {
        if (!membershipPlanRepository.existsById(command.membershipPlanId())) {
            throw new RuntimeException("Membership Plan with ID " + command.membershipPlanId() + " does not exist.");
        }
        var membershipPlan = membershipPlanRepository.findById(command.membershipPlanId()).orElseThrow(()-> new RuntimeException("Membership Plan with ID" + command.membershipPlanId()+"does not exists"));

        var benefit = new Benefit(command);
        try {
            benefitRepository.save(benefit);
            membershipPlan.addBenefit(benefit);
            membershipPlanRepository.save(membershipPlan);
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
        if (!membershipPlanRepository.existsById(command.membershipPlanId())) {
            throw new RuntimeException("Membership Plan with ID " + command.membershipPlanId() + " does not exist.");
        }
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
        var benefit = benefitRepository.findById(command.benefitId())
            .orElseThrow(() -> new RuntimeException("Benefit with ID " + command.benefitId() + " does not exist."));
        var membershipPlan = membershipPlanRepository.findById(benefit.getMembershipPlanId())
            .orElseThrow(() -> new RuntimeException(
                "[BenefitCommandServiceImpl] MembershipPlan with ID " + benefit.getMembershipPlanId() + " not found for Benefit " + command.benefitId()));
        try {
            membershipPlan.removeBenefit(command.benefitId());
            membershipPlanRepository.save(membershipPlan);
            benefitRepository.delete(benefit);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting Benefit: " + e.getMessage(), e);
        }
    }
}
