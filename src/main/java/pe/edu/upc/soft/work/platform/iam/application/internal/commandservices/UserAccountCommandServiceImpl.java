package pe.edu.upc.soft.work.platform.iam.application.internal.commandservices;

import jakarta.transaction.Transactional;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.google.GoogleTokenService;
import pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.hashing.HashingService;
import pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.tokens.TokenService;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.CreateUserAccountCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.DeleteUserAccountCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.GoogleSignInCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.SignInCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.UpdateUserAccountCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.events.UserAccountCreatedEvent;
import pe.edu.upc.soft.work.platform.iam.domain.services.UserAccountCommandService;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.EmployeeProfileRepository;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.RRHHProfileRepository;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.UserAccountRepository;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;

import java.util.Optional;

/**
 * Service implementation for handling UserAccount commands.
 */
@Service
public class UserAccountCommandServiceImpl implements UserAccountCommandService {

    private final UserAccountRepository userAccountRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final RRHHProfileRepository rrhhProfileRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final GoogleTokenService googleTokenService;

    /**
     * Constructor for UserAccountCommandServiceImpl.
     * @param userAccountRepository     the repository for UserAccount persistence
     * @param employeeProfileRepository the repository for EmployeeProfile persistence
     * @param rrhhProfileRepository     the repository for RRHHProfile persistence
     * @param hashingService            the service for password hashing
     * @param tokenService              the service for token management
     * @param userRepository            the repository for User persistence
     * @param eventPublisher            the application event publisher
     * @param googleTokenService        the outbound service for Google id_token validation
     */
    public UserAccountCommandServiceImpl(UserAccountRepository userAccountRepository,
                                         EmployeeProfileRepository employeeProfileRepository,
                                         RRHHProfileRepository rrhhProfileRepository,
                                         HashingService hashingService,
                                         TokenService tokenService,
                                         UserRepository userRepository,
                                         ApplicationEventPublisher eventPublisher,
                                         GoogleTokenService googleTokenService) {
        this.userAccountRepository = userAccountRepository;
        this.employeeProfileRepository =employeeProfileRepository;
        this.rrhhProfileRepository = rrhhProfileRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.googleTokenService = googleTokenService;

    }

    /**
     * Handles the creation of a UserAccount and publishes a creation event.
     * @param command the command to create a UserAccount
     * @return the generated ID of the new UserAccount
     */
    @Override
    public Long handle(CreateUserAccountCommand command) {
        if (userAccountRepository.findByEmail(command.email()).isPresent()) {
            throw new IllegalArgumentException(
                    "A UserAccount with email '" + command.email() + "' already exists.");
        }
        var userAccount = new UserAccount(command);
        try{
            userAccountRepository.save(userAccount);
        }catch (Exception e){
            throw new IllegalArgumentException("Error saving user account: %s".formatted(e.getMessage()));
        }
        eventPublisher.publishEvent(new UserAccountCreatedEvent(
            this,
            userAccount.getId(),
            userAccount.getUserId(),
            userAccount.getCompanyId().CompanyId()  // Long del value object
        ));
        return userAccount.getId();

    }

    /**
     * Handles the update of an existing UserAccount.
     * @param command the command to update a UserAccount
     * @return the updated UserAccount as an Optional
     */
    @Override
    public Optional<UserAccount> handle(UpdateUserAccountCommand command) {
        var accountId = command.userAccountId();
        var result = userAccountRepository.findById(accountId).get();
        result.UpdateUserAccount(command);
        try {
            var updatedAccount = userAccountRepository.save(result);
            return Optional.of(updatedAccount);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while updating user account: %s".formatted(e.getMessage()));
        }
    }

    /**
     * Handles the deletion of a UserAccount.
     * @param command the command to delete a UserAccount
     */
    @Override
    public void handle(DeleteUserAccountCommand command) {
        var accountId = command.userAccountId();
        if (!userAccountRepository.existsById(accountId))
            throw new IllegalArgumentException("User account with id %s not found".formatted(accountId));
        try {
            userAccountRepository.deleteById(accountId);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting user account: %s".formatted(e.getMessage()));
        }
    }

    /**
     * Handles the user sign-in process, verifying credentials and generating an access token.
     * @param command the command containing sign-in credentials
     * @return an Optional containing a pair of UserAccount and authentication token
     */
    @Transactional
    @Override
    public Optional<ImmutablePair<UserAccount, String>> handle(SignInCommand command) {
        var userAccount = userAccountRepository.findByEmail(command.email());
        if (userAccount.isEmpty())
            throw new IllegalArgumentException("User Account not found");
        if (!hashingService.matches(command.password(), userAccount.get().getPassword()))
            throw new IllegalArgumentException("Invalid password");
        var token = tokenService.generateToken(userAccount.get().getEmail());
        return Optional.of(ImmutablePair.of(userAccount.get(), token));
    }

    /**
     * Handles the Google sign-in process for an already registered account.
     * The Google id_token is validated through the outbound {@link GoogleTokenService};
     * if a local UserAccount already exists for the verified email, an application access
     * token is generated and returned. When no account exists the result is empty, signalling
     * that the caller must complete registration through the Google sign-up endpoints.
     * No user is provisioned here, so no placeholder data is ever persisted.
     * @param command the command containing the Google id_token
     * @return an Optional containing a pair of UserAccount and application access token when the
     *         account exists, or an empty Optional when registration is still required
     */
    @Transactional
    @Override
    public Optional<ImmutablePair<UserAccount, String>> handle(GoogleSignInCommand command) {
        var googleUserInfo = googleTokenService.verify(command.idToken());
        return userAccountRepository.findByEmail(googleUserInfo.email())
                .map(userAccount -> ImmutablePair.of(userAccount, tokenService.generateToken(userAccount.getEmail())));
    }
}
