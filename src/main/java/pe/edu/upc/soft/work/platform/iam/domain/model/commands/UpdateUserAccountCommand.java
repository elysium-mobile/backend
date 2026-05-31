package pe.edu.upc.soft.work.platform.iam.domain.model.commands;

import pe.edu.upc.soft.work.platform.iam.domain.model.valueobjects.CompanyId;
import pe.edu.upc.soft.work.platform.iam.domain.model.valueobjects.MembershipId;

import java.util.Objects;

/**
 * Command to update an existing user account
 * @param userAccountId the id of the user account to be updated
 * @param email the email of the user account to be updated
 * @param password the password of the user account to be updated
 * @param anonymousName the anonymous name of the user account to be updated
 */
public record UpdateUserAccountCommand(Long userAccountId, String email, String password, String anonymousName, MembershipId membershipId, CompanyId companyId) {

    /**
     * Constructor with validation
     * @param userAccountId the id of the user account to be updated
     * @param email the email of the user account to be updated
     * @param password the password of the user account to be updated
     * @param anonymousName the anonymous name of the user account to be updated
     */
        public UpdateUserAccountCommand{
            Objects.requireNonNull(userAccountId, "[UpdateUserAccountCommand] user account id must not be null");
            Objects.requireNonNull(email, "[UpdateUserAccountCommand] email must not be null");
            Objects.requireNonNull(password, "[UpdateUserAccountCommand] password must not be null");
            Objects.requireNonNull(anonymousName, "[UpdateUserAccountCommand] anonymous name must not be null");
            Objects.requireNonNull(membershipId, "[UpdateUserAccountCommand] membershipId name must not be null");
            Objects.requireNonNull(companyId, "[UpdateUserAccountCommand] companyId name must not be null");
        }

}
