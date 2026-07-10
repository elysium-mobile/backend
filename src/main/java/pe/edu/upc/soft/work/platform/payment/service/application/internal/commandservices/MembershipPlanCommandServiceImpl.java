package pe.edu.upc.soft.work.platform.payment.service.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.AddBenefitToMembershipPlan;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.MembershipPlan;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateMembershipPlanCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateMembershipPlanCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.DeleteMembershipPlanCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.MembershipPlanCommandService;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.BenefitRepository;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.MembershipPlanRepository;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.MembershipRepository;

import java.util.Optional;

/**
 * Service implementation for handling MembershipPlan commands
 */
@Service
public class MembershipPlanCommandServiceImpl implements MembershipPlanCommandService {
    private final MembershipPlanRepository membershipplanRepository;
    private final MembershipRepository membershipRepository;
    private final BenefitRepository benefitRepository;

    /**
     * Constructor for MembershipPlanCommandServiceImpl
     * @param membershipplanRepository the repository for MembershipPlan persistence
     */
    public MembershipPlanCommandServiceImpl(MembershipPlanRepository membershipplanRepository,
                                            MembershipRepository membershipRepository,
                                            BenefitRepository benefitRepository) {
        this.membershipplanRepository = membershipplanRepository;
        this.membershipRepository = membershipRepository;
        this.benefitRepository = benefitRepository;
    }


    /**
     * Handles the creation of a MembershipPlan
     * @param command the command to create a MembershipPlan
     * @return the generated ID of the new MembershipPlan
     */
    @Override
    public Long handle(CreateMembershipPlanCommand command) {
        var membershipplan = new MembershipPlan(command);
        try {
            membershipplanRepository.save(membershipplan);
        } catch (Exception e) {
            throw new RuntimeException("Error creating MembershipPlan: " + e.getMessage(), e);
        }
        return membershipplan.getId();
    }

    /**
     * Handles the updated of an existing MembershipPlan
     * @param command the command to update a MembershipPlan
     * @return the updated MembershipPlan as an Optional
     */
    @Override
    public Optional<MembershipPlan> handle(UpdateMembershipPlanCommand command) {
        var membershipplanId = command.membershipplanId();
        if (!this.membershipplanRepository.existsById(membershipplanId)) {
            throw new RuntimeException("MembershipPlan with ID " + membershipplanId + " does not exist.");
        }

        var membershipplanToUpdate = this.membershipplanRepository.findById(membershipplanId).get();
        membershipplanToUpdate.updateMembershipPlan(command);
        try {
            var updatedMembershipPlan = this.membershipplanRepository.save(membershipplanToUpdate);
            return Optional.of(updatedMembershipPlan);
        } catch (Exception e) {
            throw new RuntimeException("Error updating MembershipPlan: " + e.getMessage(), e);
        }
    }

    /**
     *  Handles the deletion of a MembershipPlan
     * @param command the command to delete a MembershipPlan
     */
    @Override
    public void handle(DeleteMembershipPlanCommand command) {
        if (!membershipplanRepository.existsById(command.membershipplanId())) {
            throw new RuntimeException("MembershipPlan with ID " + command.membershipplanId() + " does not exist.");
        }
        try {
            membershipplanRepository.deleteById(command.membershipplanId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting MembershipPlan: " + e.getMessage(), e);
        }
    }

    @Override
    public void handle(AddBenefitToMembershipPlan command) {
        var benefit = benefitRepository.findById(command.benefitId())
                .orElseThrow(() -> new RuntimeException("Benefit with ID " + command.benefitId() + " does not exist."));
        var membershipPlan = membershipplanRepository.findById(command.membershipPlanId())
                .orElseThrow(() -> new RuntimeException("MembershipPlan with ID " + command.membershipPlanId() + " does not exist."));
        try {
            membershipPlan.addBenefit(benefit);
            membershipplanRepository.save(membershipPlan);
        }catch (IllegalStateException ex){
            throw new IllegalArgumentException("Domain error while adding Benefit to MembershipPlan: " + ex.getMessage());
        }catch (Exception ex){
            throw new IllegalArgumentException("Error adding Benefit to MembershipPlan: " + ex.getMessage());
        }
    }
}
