package pe.edu.upc.soft.work.platform.iam.domain.services;

import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetAllUserAccountQuery;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetAllUsersQuery;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetUserAccountByIdQuery;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetUserByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying user accounts in the system.
 */
public interface UserAccountQueryService {

    /**
     * Handles the retrieval of all user accounts in the system based on the provided query.
     * @param query the query containing the necessary information to retrieve all user accounts
     * @return a list of user accounts that match the criteria specified in the query
     */
    List<UserAccount> handle(GetAllUserAccountQuery query);

    /**
     * Handles the retrieval of a user account by its identifier based on the provided query.
     * @param query the query containing the identifier of the user account to be retrieved
     * @return an Optional containing the user account if found, or an empty Optional if no user account with the specified identifier exists
     */
    Optional<UserAccount> handle(GetUserAccountByIdQuery query);
}
