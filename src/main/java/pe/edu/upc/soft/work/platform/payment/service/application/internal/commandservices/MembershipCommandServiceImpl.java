package pe.edu.upc.soft.work.platform.payment.service.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates.Membership;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateMembershipCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateMembershipCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.DeleteMembershipCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.MembershipCommandService;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.MembershipRepository;

import java.util.Optional;

@Service
public class MembershipCommandServiceImpl implements MembershipCommandService {
    private final MembershipRepository membershipRepository;

    public MembershipCommandServiceImpl(MembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }

    @Override
    public Long handle(CreateMembershipCommand command) {
        var membership = new Membership(command);
        try {
            membershipRepository.save(membership);
        } catch (Exception e) {
            throw new RuntimeException("Error creating Membership: " + e.getMessage(), e);
        }
        return membership.getId();
    }

    @Override
    public Optional<Membership> handle(UpdateMembershipCommand command) {
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
