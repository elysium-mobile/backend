package pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.AreaCompany;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.util.ArrayList;
import java.util.List;


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

    @Getter
    @Column(name = "employees", nullable = true)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAccount> employees;

    @Getter
    @Column(name = "area_company_list", nullable = true)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AreaCompany> areaCompanyList;

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
        this.areaCompanyList = command.areaCompanyList();
        this.employees = command.employees();
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

    public void addEmployee(UserAccount employee){
        if (this.employees ==null){
            this.employees = new ArrayList<>();
        }
        boolean alreadyExists = this.employees.stream()
                .anyMatch(e -> e.getId().equals(employee.getId()));
        if (!alreadyExists) {
            this.employees.add(employee);
        }
    }

    public void addAreaCompany(AreaCompany areaCompany){
        if (this.areaCompanyList == null) {
            this.areaCompanyList = new ArrayList<>();
        }
        boolean alreadyExists = this.areaCompanyList.stream()
                .anyMatch(a -> a.getId().equals(areaCompany.getId()));
        if (alreadyExists) {
            throw new IllegalStateException(
                    "AreaCompany with ID " + areaCompany.getId() + " is already assigned to this company.");
        }
        this.areaCompanyList.add(areaCompany);
    }
}
