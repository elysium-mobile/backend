package pe.edu.upc.soft.work.platform.iam.application.internal.queryservices;


import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.User;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetAllUsersQuery;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetUserByIdQuery;
import pe.edu.upc.soft.work.platform.iam.domain.services.UserQueryService;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the UserQueryService interface that handles user-related queries such as retrieving all users and retrieving a user by ID.
 */
@Service
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;

    /**
     * Constructor for UserQueryServiceImpl that takes a UserRepository as a parameter.
     * @param userRepository the UserRepository used to access user data from the database
     */
    public UserQueryServiceImpl(UserRepository userRepository) {
       this.userRepository=userRepository;

    }
    /**
     * Handles the GetAllUsersQuery by retrieving all users from the database using the UserRepository.
     * @param query the GetAllUsersQuery containing any necessary parameters for the query (in this case, there are none)
     * @return a list of all users retrieved from the database
     */
    @Override
    public List<User> handle(GetAllUsersQuery query) {
        return userRepository.findAll();
    }

    /**
     * Handles the GetUserByIdQuery by retrieving a user with the specified ID from the database using the UserRepository.
     * @param query the query containing the user ID
     * @return an Optional containing the user if found, or an empty Optional if no user with the specified ID exists in the database
     */
    @Override
    public Optional<User> handle(GetUserByIdQuery query) {
        return userRepository.findById(query.userId());
    }
}
