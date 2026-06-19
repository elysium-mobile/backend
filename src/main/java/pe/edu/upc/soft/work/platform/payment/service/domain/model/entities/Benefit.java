package pe.edu.upc.soft.work.platform.payment.service.domain.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateBenefitCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateBenefitCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;


/**
 * Benefit aggregate root entity.
 */
@Entity
@Table(name="benefits")
public class Benefit extends AuditableAbstractAggregateRoot<Benefit> {

    @Getter
    @Column(name = "title", nullable = false)
    private String title;
    @Getter
    @Column(name = "description", nullable = false)
    private String description;

    @Setter
    @Getter
    @Column(name = "membership_plan_id", nullable = false)
    private Long membershipPlanId;

    /**
     * Default constructor for JPA.
     */
    public Benefit() {}

    /**
     * Constructor to create a Benefit from a CreateBenefitCommand.
     * @param command the command containing benefit details
     */
    public Benefit(CreateBenefitCommand command) {
        this.title = command.title();
        this.description = command.description();
        this.membershipPlanId = command.membershipPlanId();
    }

    /**
     * Updates the Benefit with details from an UpdateBenefitCommand.
     * @param command the command containing updated benefit details
     */
    public void updateBenefit(UpdateBenefitCommand command) {
        this.title = command.title();
        this.description = command.description();
        this.membershipPlanId= command.membershipPlanId();
    }
}
