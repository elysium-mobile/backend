package pe.edu.upc.soft.work.platform.payment.service.domain.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
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
    private String title;
    @Getter
    private String description;

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
    }

    /**
     * Updates the Benefit with details from an UpdateBenefitCommand.
     * @param command the command containing updated benefit details
     */
    public void updateBenefit(UpdateBenefitCommand command) {
        this.title = command.title();
        this.description = command.description();
    }
}
