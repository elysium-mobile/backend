package pe.edu.upc.soft.work.platform.payment.service.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates.Membership;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateMembershipCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateMembershipCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.DeleteMembershipCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.MembershipCommandService;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.MembershipPlanRepository;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.MembershipRepository;

import java.util.Optional;

/**
 * Service implementation for handling Membership commands
 */
@Service
public class MembershipCommandServiceImpl implements MembershipCommandService {
    private final MembershipRepository membershipRepository;
    private final MembershipPlanRepository membershipPlanRepository;

    /**
     * Constructor for MembershipCommandServiceImpl
     * @param membershipRepository the repository for Membership persistence
     */
    public MembershipCommandServiceImpl(MembershipRepository membershipRepository,
                                        MembershipPlanRepository membershipPlanRepository) {
        this.membershipRepository = membershipRepository;
        this.membershipPlanRepository = membershipPlanRepository;
    }

    /**
     * Handles the creation of a Membership
     * @param command the command to create a Membership
     * @return the generated ID of the new Membership
     */
    @Override
    public Long handle(CreateMembershipCommand command) {
        if (!membershipPlanRepository.existsById(command.membershipPlanId())) {
            throw new RuntimeException("Membership Plan with ID " + command.membershipPlanId() + " does not exist.");
        }
        var membership = new Membership(command);
        try {
            membershipRepository.save(membership);
        } catch (Exception e) {
            throw new RuntimeException("Error creating Membership: " + e.getMessage(), e);
        }
        return membership.getId();
    }

    /**
     * Handles the update of an existing Membership
     * @param command the command to update a Membership
     * @return the updated Membership as an Optional
     */
    @Override
    public Optional<Membership> handle(UpdateMembershipCommand command) {
        if (!membershipPlanRepository.existsById(command.membershipPlanId())) {
            throw new RuntimeException("Membership Plan with ID " + command.membershipPlanId() + " does not exist.");
        }
        var membershipId = command.membershipId();
        if (!this.membershipRepository.existsById(membershipId)) {
            throw new RuntimeException("Membership with ID " + membershipId + " does not exist.");
        }

        var membershipToUpdate = this.membershipRepository.findById(membershipId).get();
        membershipToUpdate.updateMembership(command);
        try {
            var updatedMembership = this.membershipRepository.save(membershipToUpdate);
            return Optional.of(updatedMembership);
        } catch (Exception e) {
            throw new RuntimeException("Error updating Membership: " + e.getMessage(), e);
        }
    }

    /**
     * Handles the deletion of an Membership
     * @param command the command to delete a Membership
     */
    @Override
    public void handle(DeleteMembershipCommand command) {
        if (!membershipRepository.existsById(command.membershipId())) {
            throw new RuntimeException("Membership with ID " + command.membershipId() + " does not exist.");
        }
        try {
            membershipRepository.deleteById(command.membershipId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting Membership: " + e.getMessage(), e);
        }
    }
}
