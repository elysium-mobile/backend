package pe.edu.upc.soft.work.platform.iam.application.internal.commandservices;

import jakarta.transaction.Transactional;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.hashing.HashingService;
import pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.tokens.TokenService;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.CreateUserAccountCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.DeleteUserAccountCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.SignInCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.UpdateUserAccountCommand;
import pe.edu.upc.soft.work.platform.iam.domain.services.UserAccountCommandService;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.EmployeeProfileRepository;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.RRHHProfileRepository;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.UserAccountRepository;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserAccountCommandServiceImpl implements UserAccountCommandService {

    private final UserAccountRepository userAccountRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final RRHHProfileRepository rrhhProfileRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public UserAccountCommandServiceImpl(UserAccountRepository userAccountRepository,
                                         EmployeeProfileRepository employeeProfileRepository,
                                         RRHHProfileRepository rrhhProfileRepository,
                                         HashingService hashingService,
                                         TokenService tokenService,
                                         UserRepository userRepository) {
        this.userAccountRepository = userAccountRepository;
        this.employeeProfileRepository =employeeProfileRepository;
        this.rrhhProfileRepository = rrhhProfileRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

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
        return userAccount.getId();

    }

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
}
