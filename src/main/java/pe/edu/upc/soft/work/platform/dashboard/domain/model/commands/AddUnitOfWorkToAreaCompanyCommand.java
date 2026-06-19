package pe.edu.upc.soft.work.platform.dashboard.domain.model.commands;

/**
 *  Command to add a UnitOfWork to an AreaCompany.
 * @param unitOfWork  the ID of the UnitOfWork to be added
 * @param areaCompanyId the ID of the AreaCompany to which the UnitOfWork will be added
 */
public record AddUnitOfWorkToAreaCompanyCommand(Long unitOfWork, Long areaCompanyId) {
}
