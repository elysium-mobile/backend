package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateUnitOfWorkCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateUnitOfWorkCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.UnitOfWork;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.CreateAreaCompanyRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.CreateUnitOfWorkRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.UnitOfWorkResponse;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.UpdateUnitOfWorkRequest;

public class UnitOfWorkAssembler {

    /**
     *  Converts a CreateUnitOfWorkRequest to a CreateUnitOfWorkCommand.
     */
    public static CreateUnitOfWorkCommand toCommandFromRequest(CreateUnitOfWorkRequest request)
    {
        return new CreateUnitOfWorkCommand(request.name());
    }

    /**
     *  Converts an UpdateUnitOfWorkRequest to an UpdateUnitOfWorkCommand.
     */
    public static UpdateUnitOfWorkCommand toCommandFromRequest(Long unitOfWorkId, UpdateUnitOfWorkRequest request)
    {
        return new UpdateUnitOfWorkCommand(unitOfWorkId, request.name());
    }

    /**
     * Converts a UnitOfWork entity to a UnitOfWorkResponse.
     */
    public static UnitOfWorkResponse toResponseFromEntity(UnitOfWork unitOfWork){
        return new UnitOfWorkResponse(unitOfWork.getId(), unitOfWork.getName());
    }
}
