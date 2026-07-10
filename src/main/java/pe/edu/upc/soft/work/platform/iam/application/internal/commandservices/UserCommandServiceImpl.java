package pe.edu.upc.soft.work.platform.iam.application.internal.commandservices;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.User;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.CreateUserCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.DeleteUserCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.UpdateUserCommand;
import pe.edu.upc.soft.work.platform.iam.domain.services.UserCommandService;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;

import java.util.Optional;

/**
 * Service implementation for handling User commands.
 */
@Service
@Transactional
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;

    /**
     * Constructor for UserCommandServiceImpl.
     * @param userRepository the repository for User persistence
     */
    public UserCommandServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Handles the creation of a User.
     * @param command the command to create a User
     * @return the generated ID of the new User
     */
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

    /**
     * Handles the update of an existing User.
     * @param command the command to update a User
     * @return the updated User as an Optional
     */
    @Override
    public Optional<User> handle(UpdateUserCommand command) {
        var userId = command.userId();
        if (!this.userRepository.existsById(userId)){
            throw new RuntimeException("User with ID " + userId + " does not exist.");
        }

        var userToUpdate = this.userRepository.findById(userId).get();
        userToUpdate.updateUser(command);
        try{
            this.userRepository.save(userToUpdate);
            return Optional.of(userToUpdate);
        } catch (Exception e) {
            throw new RuntimeException("Error updating user: " + e.getMessage(), e);
        }
    }

    /**
     * Handles the deletion of a User.
     * @param command the command to delete a User
     */
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
