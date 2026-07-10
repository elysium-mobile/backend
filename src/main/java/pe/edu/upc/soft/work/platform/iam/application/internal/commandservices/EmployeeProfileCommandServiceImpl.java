package pe.edu.upc.soft.work.platform.iam.application.internal.commandservices;

import jakarta.transaction.Transactional;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.acl.ExternalDashboardServiceFromIAM;
import pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.google.GoogleTokenService;
import pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.hashing.HashingService;
import pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.tokens.TokenService;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.User;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.*;
import pe.edu.upc.soft.work.platform.iam.domain.model.entities.EmployeeProfile;
import pe.edu.upc.soft.work.platform.iam.domain.model.valueobjects.CompanyId;
import pe.edu.upc.soft.work.platform.iam.domain.model.valueobjects.MembershipId;
import pe.edu.upc.soft.work.platform.iam.domain.model.valueobjects.WorkOfTeamId;
import pe.edu.upc.soft.work.platform.iam.domain.services.EmployeeProfileCommandService;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.EmployeeProfileRepository;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.UserAccountRepository;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;

import java.util.Optional;
import java.util.UUID;


/**
 * Service implementation for handling EmployeeProfile commands.
 */
@Service
public class EmployeeProfileCommandServiceImpl implements EmployeeProfileCommandService {

    private final EmployeeProfileRepository employeeProfileRepository;
    private final UserAccountRepository userAccountRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final ExternalDashboardServiceFromIAM externalDashboardServiceFromIAM;
    private final GoogleTokenService googleTokenService;

    /**
     * Constructor for EmployeeProfileCommandServiceImpl.
     * @param employeeProfileRepository the repository for EmployeeProfile persistence
     * @param userAccountRepository the repository for UserAccount persistence
     * @param hashingService the service for password hashing
     * @param tokenService the service for token management
     * @param userRepository the repository for User persistence
     * @param externalDashboardServiceFromIAM the ACL service for Dashboard context interaction
     * @param googleTokenService the outbound service for Google id_token validation
     */
    public EmployeeProfileCommandServiceImpl(EmployeeProfileRepository employeeProfileRepository,
                                             UserAccountRepository userAccountRepository,
                                             HashingService hashingService,
                                             TokenService tokenService,
                                             UserRepository userRepository,
                                             ExternalDashboardServiceFromIAM externalDashboardServiceFromIAM,
                                             GoogleTokenService googleTokenService) {
        this.employeeProfileRepository = employeeProfileRepository;
        this.userAccountRepository = userAccountRepository;
        this.hashingService = hashingService;
        this.tokenService=tokenService;
        this.userRepository = userRepository;
        this.externalDashboardServiceFromIAM= externalDashboardServiceFromIAM;
        this.googleTokenService = googleTokenService;
    }

    /**
     * Handles the creation of an EmployeeProfile.
     * @param command the command to create an EmployeeProfile
     * @return the generated ID of the new EmployeeProfile
     */
    @Override
    public Long handle(CreateEmployeeProfileCommand command) {
        if(!this.userAccountRepository.existsById(command.userAccountId())){
            throw new NotFoundArgumentException(String.format("[EmployeeProfileCommandServiceImpl] User Account ID: %s not found in the IAM service",
                    command.userAccountId()));
        }

        if (!this.externalDashboardServiceFromIAM.existsWorkTeamById(command.workOfTeamId().workOfTeamId())){
            throw new NotFoundArgumentException(String.format("[EmployeeProfileCommandServiceImpl] Work Of Team ID: %s not found in the Dashboard service",
                    command.workOfTeamId().workOfTeamId()));
        }

        var employeeProfile = new EmployeeProfile(command);
        try {
            employeeProfileRepository.save(employeeProfile);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving employee profile: %s".formatted(e.getMessage()));
        }
        return employeeProfile.getId();
    }

    /**
     * Handles the update of an existing EmployeeProfile.
     * @param command the command to update an EmployeeProfile
     * @return the updated EmployeeProfile as an Optional
     */
    @Override
    public Optional<EmployeeProfile> handle(UpdateEmployeeProfileCommand command) {
        var employeeId = command.employeeProfileId();
        var employeeToUpdate = this.employeeProfileRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundArgumentException(
                        String.format("[EmployeeProfileCommandServiceImpl] Employee Profile ID: %s not found", employeeId)));
        employeeToUpdate.updateEmployeeProfile(command);
        try {
            var updatedEmployeeProfile = employeeProfileRepository.save(employeeToUpdate);
            return Optional.of(updatedEmployeeProfile);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while updating employee profile: %s".formatted(e.getMessage()));
        }
    }

    /**
     * Handles the deletion of an EmployeeProfile.
     * @param command the command to delete an EmployeeProfile
     */
    @Override
    public void handle(DeleteEmployeeProfileCommand command) {
        if (!employeeProfileRepository.existsById(command.employeeProfileId()))
            throw new IllegalArgumentException("Employee profile with id %s not found".formatted(command.employeeProfileId()));
        try {
            employeeProfileRepository.deleteById(command.employeeProfileId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting employee profile: %s".formatted(e.getMessage()));
        }
    }

    /**
     * Handles the sign-up process for a new employee, creating User, UserAccount and EmployeeProfile.
     * @param command the command containing sign-up details
     * @return an Optional containing the created EmployeeProfile
     */
    @Transactional
    @Override
    public Optional<EmployeeProfile> handle(EmployeeSignUpCommand command) {
        if (userAccountRepository.existsByEmail(command.email())) {
            throw new IllegalArgumentException("Email already exists");
        }


        var user = new User(new CreateUserCommand(
                command.name(),
                command.lastName(),
                command.phoneNumber(),
                command.dni()
        ));

        try {
            userRepository.save(user);
            var userAccount = new UserAccount(new CreateUserAccountCommand(
                    user.getId(),
                    command.email(),
                    hashingService.encode(command.password()),
                    UserAccount.generateAnonymousName(),
                    new MembershipId(0L),
                    new CompanyId(0L)
            ));
            userAccountRepository.save(userAccount);


            var employeeProfile = new EmployeeProfile(new CreateEmployeeProfileCommand(
                    command.dateStart(),
                    command.position(),
                    command.salary(),
                    userAccount.getId(),
                    new WorkOfTeamId(0L)
            ));
            employeeProfileRepository.save(employeeProfile);
            return Optional.of(employeeProfile);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error during sign up: %s".formatted(e.getMessage()));
        }
    }

    /**
     * Handles the sign-up completion for an employee authenticated through Google.
     * The Google id_token is re-validated to obtain the trusted email; the User, its Google-backed
     * UserAccount (with a random encoded password, since access is delegated to Google) and the
     * EmployeeProfile are then created with the real data provided in the completion form.
     * A fresh application access token is returned so the user ends the flow authenticated.
     * @param command the command containing the Google id_token and the employee profile data
     * @return an Optional containing the created UserAccount and the application access token
     */
    @Transactional
    @Override
    public Optional<ImmutablePair<UserAccount, String>> handle(GoogleEmployeeSignUpCommand command) {
        var googleUserInfo = googleTokenService.verify(command.idToken());
        if (userAccountRepository.existsByEmail(googleUserInfo.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        var user = new User(new CreateUserCommand(
                command.name(),
                command.lastName(),
                command.phoneNumber(),
                command.dni()
        ));

        try {
            userRepository.save(user);
            var userAccount = new UserAccount(new CreateUserAccountCommand(
                    user.getId(),
                    googleUserInfo.email(),
                    hashingService.encode(UUID.randomUUID().toString()),
                    UserAccount.generateAnonymousName(),
                    new MembershipId(0L),
                    new CompanyId(0L)
            ));
            userAccountRepository.save(userAccount);

            var employeeProfile = new EmployeeProfile(new CreateEmployeeProfileCommand(
                    command.dateStart(),
                    command.position(),
                    command.salary(),
                    userAccount.getId(),
                    new WorkOfTeamId(0L)
            ));
            employeeProfileRepository.save(employeeProfile);

            var token = tokenService.generateToken(userAccount.getEmail());
            return Optional.of(ImmutablePair.of(userAccount, token));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error during Google employee sign up: %s".formatted(e.getMessage()));
        }
    }

}
