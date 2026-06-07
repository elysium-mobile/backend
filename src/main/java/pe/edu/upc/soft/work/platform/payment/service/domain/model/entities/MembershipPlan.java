package pe.edu.upc.soft.work.platform.payment.service.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateMembershipPlanCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateMembershipPlanCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.util.List;


/**
 * MembershipPlan aggregate root entity.
 */
@Entity
@Table(name="membership_plans")
public class MembershipPlan extends AuditableAbstractAggregateRoot<MembershipPlan> {

    @Getter
    @Column(name = "plan_name", nullable = false)
    private String planName;

    @Getter
    @Column(name = "price", nullable = false)
    private Integer price;

    @Getter
    @Column(name = "benefit", nullable = false)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Benefit> benefits;

    @Getter
    @Column(name = "membership_id", nullable = false)
    private Long membershipId;

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
        this.price = command.price();
        this.benefits=command.benefits();
    }

    /**
     * Updates the MembershipPlan with details from an UpdateMembershipPlanCommand.
     * @param command the command containing updated membershipplan details
     */
    public void updateMembershipPlan(UpdateMembershipPlanCommand command) {
        this.planName = command.planName();
        this.price = command.price();
    }
}
