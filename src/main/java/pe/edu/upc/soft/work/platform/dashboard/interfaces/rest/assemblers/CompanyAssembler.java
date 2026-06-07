package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates.Company;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.*;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.UserAccountResponse;

import java.util.ArrayList;
import java.util.List;

public class CompanyAssembler {

    /**
     * Converts a CreateCompanyRequest to a CreateCompanyCommand.
     */
    public static CreateCompanyCommand toCommandFromRequest(CreateCompanyRequest request) {
        return new CreateCompanyCommand(request.name(), request.RUC(), request.contactEmail(), request.contactPhone(),new ArrayList<>(),new ArrayList<>());
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

        List<UserAccountResponse> employees = company.getEmployees().stream()
                .map(employee -> new UserAccountResponse(
                        employee.getId(),
                        employee.getUserId(),
                        employee.getEmail(),
                        employee.getPassword(),
                        employee.getAnonymousName(),
                        employee.getMembershipId().MembershipId(),
                        employee.getCompanyId().CompanyId()
                ))
                .toList();

        List<AreaCompanyResponse> areaCompanyResponses = company.getAreaCompanyList().stream()
                .map(areaCompany -> {
                    List<UnitOfWorkResponse> unitOfWorkResponses = areaCompany.getUnitOfWorkList().stream()
                            .map(unitOfWork -> {
                                List<WorkTeamResponse> workTeamResponses = unitOfWork.getWorkTeamList().stream()
                                        .map(workTeam -> new WorkTeamResponse(
                                                workTeam.getId(),
                                                workTeam.getTeamName(),
                                                workTeam.getLeaderOfTeam()
                                        ))
                                        .toList();
                                return new UnitOfWorkResponse(
                                        unitOfWork.getId(),
                                        unitOfWork.getName(),
                                        workTeamResponses
                                );
                            })
                            .toList();
                    return new AreaCompanyResponse(
                            areaCompany.getId(),
                            areaCompany.getName(),
                            areaCompany.getAnnualBudget(),
                            unitOfWorkResponses
                    );
                })
                .toList();


        return new CompanyResponse(company.getId(), company.getName(), company.getRUC(), company.getContactEmail(), company.getContactPhone(), employees, areaCompanyResponses);
    }
}
