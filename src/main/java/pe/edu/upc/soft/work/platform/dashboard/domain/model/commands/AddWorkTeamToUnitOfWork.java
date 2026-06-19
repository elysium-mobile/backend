package pe.edu.upc.soft.work.platform.dashboard.domain.model.commands;

/**
 *  Command to add a WorkTeam to a UnitOfWork.
 * @param widgetId  the ID of the WorkTeam to be added
 * @param unitOfWorkId  the ID of the UnitOfWork to which the WorkTeam will be added
 */
public record AddWorkTeamToUnitOfWork(Long widgetId, Long unitOfWorkId) {
}
