package pe.edu.upc.soft.work.platform.payment.service.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.MembershipPlan;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateMembershipPlanCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateMembershipPlanCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.DeleteMembershipPlanCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.MembershipPlanCommandService;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.MembershipPlanRepository;

import java.util.Optional;

/**
 * Service implementation for handling MembershipPlan commands
 */
@Service
public class MembershipPlanCommandServiceImpl implements MembershipPlanCommandService {
    private final MembershipPlanRepository membershipplanRepository;

    /**
     * Constructor for MembershipPlanCommandServiceImpl
     * @param membershipplanRepository the repository for MembershipPlan persistence
     */
    public MembershipPlanCommandServiceImpl(MembershipPlanRepository membershipplanRepository) {
        this.membershipplanRepository = membershipplanRepository;
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
}
