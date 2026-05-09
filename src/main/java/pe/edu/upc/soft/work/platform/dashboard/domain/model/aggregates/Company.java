package pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates;

import jakarta.persistence.Entity;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateCompanyCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;


/**
 * Company aggregate root entity.
 */
@Entity
public class Company extends AuditableAbstractAggregateRoot<Company> {

    @Getter
    private String name;
    @Getter
    private String RUC;
    @Getter
    private String contactEmail;
    @Getter
    private String contactPhone;

    /**
     * Default constructor for JPA.
     */
    public Company() {}

    /**
     * Constructor to create a Company from a CreateCompanyCommand.
     * @param command the command containing company details
     */
    public Company(CreateCompanyCommand command) {
        this.name = command.name();
        this.RUC = command.RUC();
        this.contactEmail = command.contactEmail();
        this.contactPhone = command.contactPhone();
    }

    /**
     * Updates the Company with details from an UpdateCompanyCommand.
     * @param command the command containing updated company details
     */
    public void updateCompany(UpdateCompanyCommand command) {
        this.name = command.name();
        this.RUC = command.RUC();
        this.contactEmail = command.contactEmail();
        this.contactPhone = command.contactPhone();
    }
}
