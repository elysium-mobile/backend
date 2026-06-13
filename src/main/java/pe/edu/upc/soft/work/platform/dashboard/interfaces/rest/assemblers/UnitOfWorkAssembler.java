package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.AddWorkTeamToUnitOfWork;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateUnitOfWorkCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateUnitOfWorkCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.UnitOfWork;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.*;

import java.util.ArrayList;
import java.util.List;

public class UnitOfWorkAssembler {

    /**
     *  Converts a CreateUnitOfWorkRequest to a CreateUnitOfWorkCommand.
     */
    public static CreateUnitOfWorkCommand toCommandFromRequest(CreateUnitOfWorkRequest request)
    {
        return new CreateUnitOfWorkCommand(request.name(), new ArrayList<>());
    }

    /**
     *  Converts an UpdateUnitOfWorkRequest to an UpdateUnitOfWorkCommand.
     */
    public static UpdateUnitOfWorkCommand toCommandFromRequest(Long unitOfWorkId, UpdateUnitOfWorkRequest request)
    {
        return new UpdateUnitOfWorkCommand(unitOfWorkId, request.name());
    }

    /**
     *  Converts an AddWorkTeamToUnitOFWorkRequest to an AddWorkTeamToUnitOfWork command.
     * @param unitOfWorkId  the unique identifier of the UnitOfWork to which the WorkTeam will be added
     * @param request   the request object containing the details of the WorkTeam to be added
     * @return  an AddWorkTeamToUnitOfWork command with the necessary information to add a WorkTeam to a UnitOfWork
     */
    public static AddWorkTeamToUnitOfWork toCommandFromRequest(Long unitOfWorkId, AddWorkTeamToUnitOFWorkRequest request)
    {
        return new AddWorkTeamToUnitOfWork(request.workTeamId(), unitOfWorkId);
    }

    /**
     * Converts a UnitOfWork entity to a UnitOfWorkResponse.
     */
    public static UnitOfWorkResponse toResponseFromEntity(UnitOfWork unitOfWork){
        List<WorkTeamResponse> workTeamResponses = unitOfWork.getWorkTeamList().stream()
                .map(workTeam -> new WorkTeamResponse(workTeam.getId(), workTeam.getTeamName(),workTeam.getLeaderOfTeam(), workTeam.getUnitOfWorkId())).toList();

        return new UnitOfWorkResponse(unitOfWork.getId(), unitOfWork.getName(), workTeamResponses);
    }
}
