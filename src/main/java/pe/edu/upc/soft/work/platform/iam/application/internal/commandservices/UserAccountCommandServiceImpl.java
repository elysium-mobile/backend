package pe.edu.upc.soft.work.platform.iam.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.CreateUserAccountCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.DeleteUserAccountCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.UpdateUserAccountCommand;
import pe.edu.upc.soft.work.platform.iam.domain.services.UserAccountCommandService;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.UserAccountRepository;

import java.util.Optional;

@Service
public class UserAccountCommandServiceImpl implements UserAccountCommandService {

    private final UserAccountRepository userAccountRepository;

    public UserAccountCommandServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public Long handle(CreateUserAccountCommand command) {
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
}
