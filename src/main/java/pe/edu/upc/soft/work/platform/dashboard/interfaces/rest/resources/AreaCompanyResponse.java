package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

/**
 *  Response DTO for AreaCompany resource.
 * */
public record AreaCompanyResponse(

    Long areaCompanyId,
    String name,
    Integer annualBudget

) {
}
