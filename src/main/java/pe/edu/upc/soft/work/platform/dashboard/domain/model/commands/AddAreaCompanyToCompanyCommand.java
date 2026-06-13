package pe.edu.upc.soft.work.platform.dashboard.domain.model.commands;

/**
 *  Command to add an AreaCompany to a Company.
 * @param areaCompanyId the ID of the AreaCompany to be added
 * @param companyId the ID of the Company to which the AreaCompany will be added
 */
public record AddAreaCompanyToCompanyCommand(Long areaCompanyId, Long companyId) {
}
