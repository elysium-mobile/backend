package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates.Company;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.CreateCompanyRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.UpdateCompanyRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.CompanyResponse;

public class CompanyAssembler {

    /**
     * Converts a CreateCompanyRequest to a CreateCompanyCommand.
     */
    public static CreateCompanyCommand toCommandFromRequest(CreateCompanyRequest request) {
        return new CreateCompanyCommand(request.name(), request.RUC(), request.contactEmail(), request.contactPhone());
    }

    /**
     * Converts an UpdateCompanyRequest to an UpdateCompanyCommand.
     */
    public static UpdateCompanyCommand toCommandFromRequest(Long companyId, UpdateCompanyRequest request) {
        return new UpdateCompanyCommand(companyId, request.name(), request.RUC(), request.contactEmail(), request.contactPhone());
    }

    /**
     * Converts a Company entity to a CompanyResponse.
     */
    public static CompanyResponse toResponseFromEntity(Company company) {
        return new CompanyResponse(company.getId(), company.getName(), company.getRUC(), company.getContactEmail(), company.getContactPhone());
    }
}
