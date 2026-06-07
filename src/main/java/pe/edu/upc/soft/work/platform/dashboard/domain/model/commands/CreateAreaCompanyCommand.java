package pe.edu.upc.soft.work.platform.dashboard.domain.model.commands;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.UnitOfWork;

import java.util.List;
import java.util.Objects;
import java.util.Date;

/**
 * Command to create a new AreaCompany
 */
public record CreateAreaCompanyCommand(String name, Integer annualBudget,
                                       List<UnitOfWork> unitOfWorkList) {

    /**
     * Constructor with validation
     */
    public CreateAreaCompanyCommand {
        Objects.requireNonNull(name, "[CreateAreaCompanyCommand] name must not be null");
        Objects.requireNonNull(annualBudget, "[CreateAreaCompanyCommand] annualBudget must not be null");
    }
}
