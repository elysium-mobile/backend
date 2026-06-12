package pe.edu.upc.soft.work.platform.iam.interfaces.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetEmployeeProfileByIdQuery;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetRRHHProfileByIdQuery;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetUserAccountByIdQuery;
import pe.edu.upc.soft.work.platform.iam.domain.services.EmployeeProfileQueryService;
import pe.edu.upc.soft.work.platform.iam.domain.services.RRHHProfileQueryService;
import pe.edu.upc.soft.work.platform.iam.domain.services.UserAccountQueryService;

/**
 * Facade for the IAM Bounded Context.
 * Provides a stable interface for other contexts to interact with user accounts and employee profiles.
 */
@Service
public class IamContextFacade {

    /**
     * Query service for user accounts.
     */
    private final UserAccountQueryService userAccountQueryService;

    /**
     * Query service for employee profiles.
     */
    private final EmployeeProfileQueryService employeeProfileQueryService;

    private final RRHHProfileQueryService rrhhProfileQueryService;

    /**
     * Constructor for IamContextFacade.
     *
     * @param userAccountQueryService      the user account query service
     * @param employeeProfileQueryService  the employee profile query service
     */
    public IamContextFacade(UserAccountQueryService userAccountQueryService,
                            EmployeeProfileQueryService employeeProfileQueryService,
                            RRHHProfileQueryService rrhhProfileQueryService) {
        this.userAccountQueryService = userAccountQueryService;
        this.employeeProfileQueryService = employeeProfileQueryService;
        this.rrhhProfileQueryService = rrhhProfileQueryService;
    }

    /**
     * Check if a user account exists by its ID.
     *
     * @param userAccountId the ID of the user account
     * @return true if the user account exists, false otherwise
     */
    public boolean existsUserAccountById(Long userAccountId) {
        var query = new GetUserAccountByIdQuery(userAccountId);
        return this.userAccountQueryService.handle(query).isPresent();
    }

    /**
     * Check if an employee profile exists by its ID.
     *
     * @param employeeProfileId the ID of the employee profile
     * @return true if the employee profile exists, false otherwise
     */
    public boolean existsEmployeeProfileById(Long employeeProfileId) {
        var query = new GetEmployeeProfileByIdQuery(employeeProfileId);
        return this.employeeProfileQueryService.handle(query).isPresent();
    }

    public boolean existsRRHHProfileById(Long RRHHProfileId){
        var query = new GetRRHHProfileByIdQuery(RRHHProfileId);
        return this.rrhhProfileQueryService.handle(query).isPresent();
    }
}
