package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.AddUnitOfWorkToAreaCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateAreaCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateAreaCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.AreaCompany;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.UnitOfWork;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.*;

import java.util.ArrayList;
import java.util.List;

public class AreaCompanyAssembler {

    /**
     *  Converts a CreateAreaCompanyRequest to a CreateAreaCompanyCommand.
     */
    public static CreateAreaCompanyCommand toCommandFromRequest(CreateAreaCompanyRequest request){
        return new CreateAreaCompanyCommand(request.name(),request.annualBudget(),request.companyId(), new ArrayList<>());
    }

    /**
     *  Converts an UpdateAreaCompanyRequest to an UpdateAreaCompanyCommand.
     */
    public static UpdateAreaCompanyCommand toCommandFromRequest(Long areaCompanyId, UpdateAreaCompanyRequest request)
    {
        return new UpdateAreaCompanyCommand(areaCompanyId, request.name(),request.annualBudget(), request.companyId());
    }

    /**
     *  Converts an AddUnitOfWorkToAreaCompanyRequest to an AddUnitOfWorkToAreaCompanyCommand.
     * @param areaCompanyId the ID of the AreaCompany to which the UnitOfWork will be added
     * @param request the request containing the ID of the UnitOfWork to be added to the AreaCompany
     * @return  an AddUnitOfWorkToAreaCompanyCommand containing the necessary information to add a UnitOfWork to an AreaCompany
     */
    public static AddUnitOfWorkToAreaCompanyCommand toCommandFromRequest(Long areaCompanyId, AddUnitOfWorkToAreaCompanyRequest request) {
        return new AddUnitOfWorkToAreaCompanyCommand(request.unitOfWorkId(), areaCompanyId);
    }

    /**
     *  Converts an AreaCompany entity to an AreaCompanyResponse.
     */
    public static AreaCompanyResponse toResponseFromEntity(AreaCompany areaCompany)
    {
        List<UnitOfWorkResponse> unitOfWorkResponses = areaCompany.getUnitOfWorkList().stream()
                .map(unitOfWork -> new UnitOfWorkResponse(
                        unitOfWork.getId(),
                        unitOfWork.getName(),
                        unitOfWork.getWorkTeamList().stream()
                                .map(workTeam -> new pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.WorkTeamResponse(
                                        workTeam.getId(),
                                        workTeam.getTeamName(),
                                        workTeam.getLeaderOfTeam(),
                                        workTeam.getUnitOfWorkId()
                                ))
                                .toList())
                )
                .toList();
        return new AreaCompanyResponse(areaCompany.getId(), areaCompany.getName(), areaCompany.getAnnualBudget(),areaCompany.getCompanyId(), unitOfWorkResponses);
    }
}
