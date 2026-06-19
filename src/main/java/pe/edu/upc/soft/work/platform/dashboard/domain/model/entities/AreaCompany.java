package pe.edu.upc.soft.work.platform.dashboard.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateAreaCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateAreaCompanyCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.util.ArrayList;
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

    @Setter
    @Getter
    @Column(name = "company_id", nullable = false)
    private Long companyId;

    /**
     * Default constructor for JPA.
     */
    public AreaCompany() {}

    /**
     * Constructor to create a AreaCompany from a CreateAreaCompanyCommand.
     * @param command the command containing areCompany details
     */
    public AreaCompany(CreateAreaCompanyCommand command) {
        this.name = command.name();
        this.annualBudget = command.annualBudget();
        this.companyId = command.companyId();
        this.unitOfWorkList = command.unitOfWorkList();
    }

    /**
     * Updates the AreaCompany with details from an UpdateAreaCompanyCommand.
     * @param command the command containing updated areaCompany details
     */
    public void updateAreaCompany(UpdateAreaCompanyCommand command) {
        this.name = command.name();
        this.annualBudget = command.annualBudget();
        this.companyId = command.companyId();
    }

    /**
     *  Adds a UnitOfWork to the AreaCompany's list of UnitOfWork.
     * @param unitOfWork    the UnitOfWork to be added
     */
    public void addUnitOfWork(UnitOfWork unitOfWork){
        if (this.unitOfWorkList == null) {
            this.unitOfWorkList = new ArrayList<>();
        }
        boolean alreadyExists = this.unitOfWorkList.stream()
                .anyMatch(u -> u.getId().equals(unitOfWork.getId()));
        if (alreadyExists) {
            throw new IllegalStateException(
                    "UnitOfWork with ID " + unitOfWork.getId() + " is already assigned to this area.");
        }
        this.unitOfWorkList.add(unitOfWork);
    }

    /**
     *  Removes a UnitOfWork from the AreaCompany's list of UnitOfWork by its ID.
     * @param unitOfWorkId  the ID of the UnitOfWork to be removed
     */
    public void removeUnitOfWork(Long unitOfWorkId) {
        if (this.unitOfWorkList == null) {
            throw new IllegalArgumentException("UnitOfWork with ID " + unitOfWorkId + " not found in this area.");
        }
        boolean removed = this.unitOfWorkList.removeIf(u -> u.getId().equals(unitOfWorkId));
        if (!removed) {
            throw new IllegalArgumentException("UnitOfWork with ID " + unitOfWorkId + " not found in this area.");
        }
    }
}
