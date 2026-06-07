package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateAreaCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateAreaCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.AreaCompany;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.UnitOfWork;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.AreaCompanyResponse;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.CreateAreaCompanyRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.UnitOfWorkResponse;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.UpdateAreaCompanyRequest;

import java.util.ArrayList;
import java.util.List;

public class AreaCompanyAssembler {

    /**
     *  Converts a CreateAreaCompanyRequest to a CreateAreaCompanyCommand.
     */
    public static CreateAreaCompanyCommand toCommandFromRequest(CreateAreaCompanyRequest request){
        return new CreateAreaCompanyCommand(request.name(),request.annualBudget(),0L, new ArrayList<>());
    }

    /**
     *  Converts an UpdateAreaCompanyRequest to an UpdateAreaCompanyCommand.
     */
    public static UpdateAreaCompanyCommand toCommandFromRequest(Long areaCompanyId, UpdateAreaCompanyRequest request)
    {
        return new UpdateAreaCompanyCommand(areaCompanyId, request.name(),request.annualBudget(), request.companyId());
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
                                        workTeam.getLeaderOfTeam()
                                ))
                                .toList())
                )
                .toList();
        return new AreaCompanyResponse(areaCompany.getId(), areaCompany.getName(), areaCompany.getAnnualBudget(),areaCompany.getCompanyId(), unitOfWorkResponses);
    }
}
