package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateAreaCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateAreaCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.AreaCompany;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.AreaCompanyResponse;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.CreateAreaCompanyRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.UpdateAreaCompanyRequest;

public class AreaCompanyAssembler {

    /**
     *  Converts a CreateAreaCompanyRequest to a CreateAreaCompanyCommand.
     */
    public static CreateAreaCompanyCommand toCommandFromRequest(CreateAreaCompanyRequest request){
        return new CreateAreaCompanyCommand(request.name(),request.annualBudget());
    }

    /**
     *  Converts an UpdateAreaCompanyRequest to an UpdateAreaCompanyCommand.
     */
    public static UpdateAreaCompanyCommand toCommandFromRequest(Long areaCompanyId, UpdateAreaCompanyRequest request)
    {
        return new UpdateAreaCompanyCommand(areaCompanyId, request.name(),request.annualBudget());
    }

    /**
     *  Converts an AreaCompany entity to an AreaCompanyResponse.
     */
    public static AreaCompanyResponse toResponseFromEntity(AreaCompany areaCompany)
    {
        return new AreaCompanyResponse(areaCompany.getId(), areaCompany.getName(), areaCompany.getAnnualBudget());
    }
}
