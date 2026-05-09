package pe.edu.upc.soft.work.platform.payment.service.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.MembershipPlan;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateMembershipPlanCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateMembershipPlanCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.DeleteMembershipPlanCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.MembershipPlanCommandService;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.MembershipPlanRepository;

import java.util.Optional;

@Service
public class MembershipPlanCommandServiceImpl implements MembershipPlanCommandService {
    private final MembershipPlanRepository membershipplanRepository;

    public MembershipPlanCommandServiceImpl(MembershipPlanRepository membershipplanRepository) {
        this.membershipplanRepository = membershipplanRepository;
    }

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
