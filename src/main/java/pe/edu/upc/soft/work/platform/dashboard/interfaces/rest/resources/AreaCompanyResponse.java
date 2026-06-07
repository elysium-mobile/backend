package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.UnitOfWork;

import java.util.List;

/**
 *  Response DTO for AreaCompany resource.
 * */
public record AreaCompanyResponse(

    Long areaCompanyId,
    String name,
    Integer annualBudget,
    Long companyId,
    List<UnitOfWorkResponse> unitOfWorkList

) {
}
