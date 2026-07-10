package pe.edu.upc.soft.work.platform.iam.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.CreateUserAccountCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.UpdateUserAccountCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.valueobjects.CompanyId;
import pe.edu.upc.soft.work.platform.iam.domain.model.valueobjects.MembershipId;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.CreateUserAccountRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.UpdateUserAccountRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.UserAccountResponse;

public class UserAccountAssembler {


    /**
     *  Converts a CreateUserAccountRequest into a CreateUserAccountCommand.
     * @param request The create user account request.
     * @return The corresponding CreateUserAccountCommand.
     */
    public static CreateUserAccountCommand toCommandFromRequest(CreateUserAccountRequest request) {
        return new CreateUserAccountCommand(
                request.userId(),
                request.email(),
                request.password(),
                request.anonymousName(),
                new MembershipId(request.membershipId()),
                new CompanyId(request.companyId())
        );
    }

    /**
     * Converts an UpdateUserAccountRequest into an UpdateUserAccountCommand.
     * @param userAccountId The user account ID to update.
     * @param request The update user account request.
     * @return The corresponding UpdateUserAccountCommand.
     */
     public static UpdateUserAccountCommand toCommandFromRequest(Long userAccountId, UpdateUserAccountRequest request) {
        return new UpdateUserAccountCommand(
                userAccountId,
                request.email(),
                request.password(),
                request.anonymousName(),
                new MembershipId(request.membershipId()),
                new CompanyId(request.companyId())
        );
    }

    /**
     * Converts a UserAccount entity into a UserAccountResponse.
     * @param entity The user account entity.
     * @return The corresponding UserAccountResponse.
     */
     public static UserAccountResponse toResponseFromEntity(UserAccount entity) {
        return new UserAccountResponse(
                entity.getId(),
                entity.getUserId(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getAnonymousName(),
                entity.getMembershipId().MembershipId(),
                entity.getCompanyId().CompanyId()
        );
    }
}
