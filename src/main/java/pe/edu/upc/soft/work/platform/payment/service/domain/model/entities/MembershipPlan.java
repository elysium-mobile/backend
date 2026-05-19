package pe.edu.upc.soft.work.platform.payment.service.domain.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateMembershipPlanCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateMembershipPlanCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;


/**
 * MembershipPlan aggregate root entity.
 */
@Entity
@Table(name="membership_plans")
public class MembershipPlan extends AuditableAbstractAggregateRoot<MembershipPlan> {

    @Getter
    private String planName;

    /**
     * Default constructor for JPA.
     */
    public MembershipPlan() {}

    /**
     * Constructor to create a MembershipPlan from a CreateMembershipPlanCommand.
     * @param command the command containing membershipplan details
     */
    public MembershipPlan(CreateMembershipPlanCommand command) {
        this.planName = command.planName();
    }

    /**
     * Updates the MembershipPlan with details from an UpdateMembershipPlanCommand.
     * @param command the command containing updated membershipplan details
     */
    public void updateMembershipPlan(UpdateMembershipPlanCommand command) {
        this.planName = command.planName();
    }
}
