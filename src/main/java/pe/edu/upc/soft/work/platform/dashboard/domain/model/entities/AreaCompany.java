package pe.edu.upc.soft.work.platform.dashboard.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateAreaCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateAreaCompanyCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.util.List;


/**
 * AreaCompany aggregate root entity.
 */
@Entity
@Table(name = "area_company")
public class AreaCompany extends AuditableAbstractAggregateRoot<AreaCompany> {

    @Getter
    @Column(name = "name", nullable = false)
    private String name;
    @Getter
    @Column(name = "annual_budget", nullable = false)
    private Integer annualBudget;

    @Getter
    @Column(name = "unit_of_work_list", nullable = true)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UnitOfWork> unitOfWorkList;

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
        this.unitOfWorkList = command.unitOfWorkList();
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
