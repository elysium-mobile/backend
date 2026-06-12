package pe.edu.upc.soft.work.platform.iam.domain.services;

import org.apache.commons.lang3.tuple.ImmutablePair;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.*;
import pe.edu.upc.soft.work.platform.iam.domain.model.entities.EmployeeProfile;

import java.util.Optional;

/**
 * Service interface for handling commands related to employee profiles.
 */
public interface EmployeeProfileCommandService {

    /**
     * Handles the creation of a new employee profile.
     * @param command the command containing the details for creating the employee profile
     * @return the identifier of the newly created employee profile
     */
    Long handle(CreateEmployeeProfileCommand command);

    /**
     * Handles the update of an existing employee profile.
     * @param command the command containing the details for updating the employee profile
     * @return an Optional containing the updated employee profile if the update was successful, or an empty Optional if the employee profile was not found
     */
    Optional<EmployeeProfile> handle(UpdateEmployeeProfileCommand command);

    /**
     * Handles the deletion of an existing employee profile.
     * @param command the command containing the identifier of the employee profile to be deleted
     */
    void handle(DeleteEmployeeProfileCommand command);


    Optional<EmployeeProfile> handle(EmployeeSignUpCommand command);

//    Optional<ImmutablePair<UserAccount, String>> handle(SignInCommand command);
}
