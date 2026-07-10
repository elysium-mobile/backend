package pe.edu.upc.soft.work.platform.dashboard.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.EmployeeProfileRepository;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.UserAccountRepository;
import pe.edu.upc.soft.work.platform.iam.interfaces.acl.IamContextFacade;

import java.util.List;
import java.util.Optional;

/**
 * Outbound service to interact with the IAM context from the Dashboard context.
 * Used to verify user account and employee profile existence when
 * managing companies, work teams, and units of work.
 */
@Service
public class ExternalIamServiceFromDashboard {

    /**
     * IAM context facade.
     */
    private final IamContextFacade iamContextFacade;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final UserAccountRepository userAccountRepository;


    /**
     * Constructor for ExternalIamServiceFromDashboard.
     *
     * @param iamContextFacade the IAM context facade
     */
    public ExternalIamServiceFromDashboard(IamContextFacade iamContextFacade,
                                           EmployeeProfileRepository employeeProfileRepository,
                                           UserAccountRepository userAccountRepository) {
        this.iamContextFacade = iamContextFacade;
        this.employeeProfileRepository = employeeProfileRepository;
        this.userAccountRepository = userAccountRepository;

    }

    /**
     * Check if a user account exists by its ID.
     *
     * @param userAccountId the ID of the user account to verify
     * @return true if the user account exists, false otherwise
     */
    public boolean existsUserAccountById(Long userAccountId) {
        return this.iamContextFacade.existsUserAccountById(userAccountId);
    }

    /**
     * Check if an employee profile exists by its ID.
     *
     * @param employeeProfileId the ID of the employee profile to verify
     * @return true if the employee profile exists, false otherwise
     */
    public boolean existsEmployeeProfileById(Long employeeProfileId) {
        return this.iamContextFacade.existsEmployeeProfileById(employeeProfileId);
    }

    /**
     * Get a UserAccount by its ID.
     *
     * @param userAccountId the ID of the user account
     * @return an Optional containing the UserAccount if found
     */
    public Optional<UserAccount> getUserAccountById(Long userAccountId) {
        return this.userAccountRepository.findById(userAccountId);
    }

    /**
     * Returns the employee-profile IDs whose userAccountId is in the given list.
     * Used by dashboard analytics to map company employees to their performance records.
     *
     * @param userAccountIds list of user-account IDs (employees of a company)
     * @return list of matching employee-profile IDs
     */
    public List<Long> getEmployeeProfileIdsByUserAccountIds(List<Long> userAccountIds) {
        if (userAccountIds == null || userAccountIds.isEmpty()) return List.of();
        return employeeProfileRepository.findAll().stream()
            .filter(ep -> userAccountIds.contains(ep.getUserAccountId()))
            .map(ep -> ep.getId())
            .toList();
    }

    /**
     * Returns the employee-profile IDs associated with the given work-team IDs.
     * Used by dashboard analytics to count employees per area.
     *
     * @param workTeamIds list of work-team IDs
     * @return list of matching employee-profile IDs
     */
    public long countEmployeeProfilesByWorkTeamIds(List<Long> workTeamIds) {
        if (workTeamIds == null || workTeamIds.isEmpty()) return 0L;
        return employeeProfileRepository.findAll().stream()
            .filter(ep -> ep.getWorkOfTeamId() != null
                && workTeamIds.contains(ep.getWorkOfTeamId().workOfTeamId()))
            .count();
    }
}
