package pe.edu.upc.soft.work.platform.iam.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetAllUserAccountQuery;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetUserAccountByIdQuery;
import pe.edu.upc.soft.work.platform.iam.domain.services.UserAccountQueryService;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.UserAccountRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserAccountQueryServiceImpl implements UserAccountQueryService {

    private final UserAccountRepository userAccountRepository;

    /**
     * Constructor for UserAccountQueryServiceImpl that takes a UserAccountRepository as a parameter.
     * @param userAccountRepository the UserAccountRepository to be used by this service
     */
    public UserAccountQueryServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    /**
     * Handles the GetAllUserAccountQuery by retrieving all user accounts from the repository.
     * @param query the query containing the necessary information to retrieve all user accounts
     * @return a list of all user accounts in the system
     */
    @Override
    public List<UserAccount> handle(GetAllUserAccountQuery query) {
        return userAccountRepository.findAll();
    }

    /**
     * Handles the GetUserAccountByIdQuery by retrieving a user account with the specified identifier from the repository.
     * @param query the query containing the identifier of the user account to be retrieved
     * @return an Optional containing the user account if found, or an empty Optional if not found
     */
    @Override
    public Optional<UserAccount> handle(GetUserAccountByIdQuery query) {
        return userAccountRepository.findById(query.UserAccountId());
    }
}
