package pe.edu.upc.soft.work.platform.dashboard.domain.model.entities;

import jakarta.persistence.Entity;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateAreaCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateAreaCompanyCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;


/**
 * AreaCompany aggregate root entity.
 */
@Entity
public class AreaCompany extends AuditableAbstractAggregateRoot<AreaCompany> {

    @Getter
    private String name;
    @Getter
    private Integer annualBudget;

    /**
     * Default constructor for JPA.
     */
    public AreaCompany() {}

    /**
     * Constructor to create a AreaCompany from a CreateAreaCompanyCommand.
     * @param command the command containing areacompany details
     */
    public AreaCompany(CreateAreaCompanyCommand command) {
        this.name = command.name();
        this.annualBudget = command.annualBudget();
    }

    /**
     * Updates the AreaCompany with details from an UpdateAreaCompanyCommand.
     * @param command the command containing updated areacompany details
     */
    public void updateAreaCompany(UpdateAreaCompanyCommand command) {
        this.name = command.name();
        this.annualBudget = command.annualBudget();
    }
}
