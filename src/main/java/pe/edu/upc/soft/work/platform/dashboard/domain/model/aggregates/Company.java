package pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateCompanyCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;


/**
 * Company aggregate root entity.
 */
@Entity
@Table(name = "companies")
public class Company extends AuditableAbstractAggregateRoot<Company> {

    @Getter
    @Column(name = "name", nullable = false)
    private String name;
    @Getter
    @Column(name = "RUC", nullable = false)
    private String RUC;
    @Getter
    @Column(name = "contact_email", nullable = false)
    private String contactEmail;
    @Getter
    @Column(name = "contact_phone", nullable = false)
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
