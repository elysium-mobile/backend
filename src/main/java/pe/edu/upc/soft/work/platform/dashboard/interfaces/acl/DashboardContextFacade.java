package pe.edu.upc.soft.work.platform.dashboard.interfaces.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.AddEmployeesToCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetCompanyByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetWorkTeamByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.CompanyCommandService;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.CompanyQueryService;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.WorkTeamQueryService;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers.CompanyAssembler;

import java.util.Objects;
import java.util.Optional;

/**
 * Facade for the Dashboard Bounded Context.
 * Exposes verification and creation operations on companies and work teams
 * for consumption by other Bounded Contexts.
 */
@Service
public class DashboardContextFacade {

    /**
     * Query service for companies.
     */
    private final CompanyQueryService companyQueryService;

    /**
     * Command service for companies.
     */
    private final CompanyCommandService companyCommandService;

    /**
     * Query service for work teams.
     */
    private final WorkTeamQueryService workTeamQueryService;

    /**
     * Constructor for DashboardContextFacade.
     *
     * @param companyQueryService   the company query service
     * @param companyCommandService the company command service
     * @param workTeamQueryService  the work team query service
     */
    public DashboardContextFacade(CompanyQueryService companyQueryService,
                                  CompanyCommandService companyCommandService,
                                  WorkTeamQueryService workTeamQueryService) {
        this.companyQueryService = companyQueryService;
        this.companyCommandService = companyCommandService;
        this.workTeamQueryService = workTeamQueryService;
    }

    /**
     * Check if a company exists by its ID.
     *
     * @param companyId the ID of the company
     * @return true if the company exists, false otherwise
     */
    public boolean existsCompanyById(Long companyId) {
        var query = new GetCompanyByIdQuery(companyId);
        return this.companyQueryService.handle(query).isPresent();
    }

    /**
     * Create a new company.
     *
     * @param name         the company name
     * @param ruc          the RUC identifier of the company
     * @param contactEmail the contact email address
     * @param contactPhone the contact phone number
     * @return the ID of the created company, or 0L if creation failed
     */
    public Long createCompany(String name, String ruc, String contactEmail, String contactPhone) {
        var createCompanyCommand = CompanyAssembler.toCommandFromRequest(
                new pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.CreateCompanyRequest(
                        name, ruc, contactEmail, contactPhone));
        var companyId = this.companyCommandService.handle(createCompanyCommand);
        if (Objects.isNull(companyId)) {
            return 0L;
        }
        return companyId;
    }

    /**
     * Check if a work team exists by its ID.
     *
     * @param workTeamId the ID of the work team
     * @return true if the work team exists, false otherwise
     */
    public boolean existsWorkTeamById(Long workTeamId) {
        var query = new GetWorkTeamByIdQuery(workTeamId);
        return this.workTeamQueryService.handle(query).isPresent();
    }

    public void addEmployeeToCompany(Long userAccountId, Long companyId){
        this.companyCommandService.handle(new AddEmployeesToCompanyCommand(userAccountId,companyId));
    }

    /**
     * Retrieves the name of a company by its ID.
     * Useful for other Bounded Contexts (e.g. the Assistant BC) that need to
     * enrich AI prompts with basic company context without depending directly
     * on the Company aggregate.
     *
     * @param companyId the ID of the company
     * @return an Optional containing the company name, or empty if not found
     */
    public Optional<String> getCompanyNameById(Long companyId) {
        var query = new GetCompanyByIdQuery(companyId);
        return this.companyQueryService.handle(query).map(
            pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates.Company::getName);
    }
}
