package pe.edu.upc.soft.work.platform.iam.domain.services;

import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.User;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetAllUsersQuery;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetUserByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying users in the system.
 */
public interface UserQueryService {

    /**
     * Retrieves a list of all users in the system.
     * @return a list of users
     */
    List<User> handle(GetAllUsersQuery query);

    /**
     * Retrieves a user by their unique identifier.
     * @param query the query containing the user ID
     * @return an Optional containing the user if found, or an empty Optional if not found
     */
    Optional<User> handle(GetUserByIdQuery query);
}
