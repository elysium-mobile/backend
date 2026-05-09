package pe.edu.upc.soft.work.platform.iam.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.User;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.CreateUserCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.DeleteUserCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.UpdateUserCommand;
import pe.edu.upc.soft.work.platform.iam.domain.services.UserCommandService;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;

    public UserCommandServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Long handle(CreateUserCommand command) {

        if (userRepository.existsByDni(command.dni())) {
            throw new RuntimeException("User with DNI " + command.dni() + " already exists.");
        }
        var user = new User(command);
        try {
            userRepository.save(user);
        }catch (Exception e) {
            throw new RuntimeException("Error creating user: " + e.getMessage(), e);
        }
        return user.getId();

    }

    @Override
    public Optional<User> handle(UpdateUserCommand command) {
        var userId = command.userId();
        if (!this.userRepository.existsById(userId)){
            throw new RuntimeException("User with ID " + userId + " does not exist.");
        }

        var userToUpdate = this.userRepository.findById(userId).get();
        userToUpdate.updateUser(command);
        try{
            var updatedUser = this.userRepository.save(userToUpdate);
            return Optional.of(updatedUser);
        } catch (Exception e) {
            throw new RuntimeException("Error updating user: " + e.getMessage(), e);
        }
    }

    @Override
    public void handle(DeleteUserCommand command) {
        if(!userRepository.existsById(command.userId())) {
            throw new RuntimeException("User with ID " + command.userId() + " does not exist.");
        }
        try {
            userRepository.deleteById(command.userId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user: " + e.getMessage(), e);
        }
    }
}
