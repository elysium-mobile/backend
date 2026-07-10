package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates.Company;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.AddAreaCompanyToCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.AddEmployeesToCompanyCommand;
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
   *  Converts an AddEmployeeToCompanyRequest to an AddEmployeesToCompanyCommand.
   * @param companyId the ID of the company to which the employee will be added
   * @param request the request containing the employee ID to be added to the company
   * @return  an AddEmployeesToCompanyCommand containing the employee ID and company ID for processing the addition of the employee to the company
   */
    public static AddEmployeesToCompanyCommand toCommandFromRequest(Long companyId, AddEmployeeToCompanyRequest request){
        return new AddEmployeesToCompanyCommand(request.employeeId(), companyId);
    }

  /**
   *  Converts an AddAreaCompanyToCompanyRequest to an AddAreaCompanyToCompanyCommand.
   * @param companyId the ID of the company to which the area company will be added
   * @param request the request containing the area company ID to be added to the company
   * @return  an AddAreaCompanyToCompanyCommand containing the area company ID and company ID for processing the addition of the area company to the company
   */
    public static AddAreaCompanyToCompanyCommand toCommandFromRequest(Long companyId, AddAreaCompanyToCompanyRequest request) {
        return new AddAreaCompanyToCompanyCommand(request.areaCompanyId(), companyId);
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
                        null,
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
                                                workTeam.getLeaderOfTeam(),
                                                workTeam.getUnitOfWorkId()
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
                            areaCompany.getCompanyId(),
                            unitOfWorkResponses
                    );
                })
                .toList();


        return new CompanyResponse(company.getId(), company.getName(), company.getRUC(), company.getContactEmail(), company.getContactPhone(), employees, areaCompanyResponses);
    }
}
