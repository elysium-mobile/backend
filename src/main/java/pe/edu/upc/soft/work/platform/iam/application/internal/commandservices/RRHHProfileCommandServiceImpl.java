package pe.edu.upc.soft.work.platform.iam.application.internal.commandservices;

import jakarta.transaction.Transactional;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.google.GoogleTokenService;
import pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.hashing.HashingService;
import pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.tokens.TokenService;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.User;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.*;
import pe.edu.upc.soft.work.platform.iam.domain.model.entities.RRHHProfile;
import pe.edu.upc.soft.work.platform.iam.domain.model.valueobjects.CompanyId;
import pe.edu.upc.soft.work.platform.iam.domain.model.valueobjects.MembershipId;
import pe.edu.upc.soft.work.platform.iam.domain.services.RRHHProfileCommandService;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.RRHHProfileRepository;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.UserAccountRepository;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;

import java.util.Optional;
import java.util.UUID;

/**
 * Service implementation for handling RRHHProfile commands.
 */
@Service
public class RRHHProfileCommandServiceImpl implements RRHHProfileCommandService {

    private final RRHHProfileRepository rrhhProfileRepository;
    private final UserAccountRepository userAccountRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final GoogleTokenService googleTokenService;

    /**
     * Constructor for RRHHProfileCommandServiceImpl.
     * @param rrhhProfileRepository the repository for RRHHProfile persistence
     * @param userAccountRepository the repository for UserAccount persistence
     * @param hashingService the service for password hashing
     * @param tokenService the service for token management
     * @param userRepository the repository for User persistence
     * @param googleTokenService the outbound service for Google id_token validation
     */
    public RRHHProfileCommandServiceImpl(RRHHProfileRepository rrhhProfileRepository,
                                         UserAccountRepository userAccountRepository,
                                         HashingService hashingService,
                                         TokenService tokenService,
                                         UserRepository userRepository,
                                         GoogleTokenService googleTokenService) {
        this.rrhhProfileRepository = rrhhProfileRepository;
        this.userAccountRepository = userAccountRepository;
        this.hashingService = hashingService;
        this.tokenService=tokenService;
        this.userRepository = userRepository;
        this.googleTokenService = googleTokenService;
    }

    /**
     * Handles the creation of an RRHHProfile.
     * @param command the command to create an RRHHProfile
     * @return the generated ID of the new RRHHProfile
     */
    @Override
    public Long handle(CreateRRHHProfileCommand command) {
        if (!userAccountRepository.existsById(command.userAccountId())){
            throw new NotFoundArgumentException(
                    String.format("[SurveyResponseCommandServiceImpl] User Account ID: %s not found in the external Feedback service",
                            command.userAccountId()));
        }

        var rrhhProfile = new RRHHProfile(command);
        try {
            rrhhProfileRepository.save(rrhhProfile);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving RRHH profile: %s".formatted(e.getMessage()));
        }
        return rrhhProfile.getId();
    }

    /**
     * Handles the update of an existing RRHHProfile.
     * @param command the command to update an RRHHProfile
     * @return the updated RRHHProfile as an Optional
     */
    @Override
    public Optional<RRHHProfile> handle(UpdateRRHHProfileCommand command) {
        var rrhhId = command.RRHHProfileId();
        var rrhhToUpdate = rrhhProfileRepository.findById(rrhhId).get();
        rrhhToUpdate.updateRRHHProfile(command);
        try {
            var updatedRRHHProfile = rrhhProfileRepository.save(rrhhToUpdate);
            return Optional.of(updatedRRHHProfile);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while updating RRHH profile: %s".formatted(e.getMessage()));
        }
    }

    /**
     * Handles the deletion of an RRHHProfile.
     * @param command the command to delete an RRHHProfile
     */
    @Override
    public void handle(DeleteRRHHProfileCommand command) {
        if (!rrhhProfileRepository.existsById(command.rrhhProfileId())) {
            throw new IllegalArgumentException("RRHH profile with id %s not found".formatted(command.rrhhProfileId()));
        }
        try {
            rrhhProfileRepository.deleteById(command.rrhhProfileId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting RRHH profile: %s".formatted(e.getMessage()));
        }

    }


    /**
     * Handles the sign-up process for a new RRHH user, creating User, UserAccount and RRHHProfile.
     * @param command the command containing RRHH sign-up details
     * @return an Optional containing the created RRHHProfile
     */
    @Transactional
    @Override
    public Optional<RRHHProfile> handle(RRHHSignUpCommand command) {
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
                    command.anonymousName(),
                    new MembershipId(0L),
                    new CompanyId(0L)
            ));

            userAccountRepository.save(userAccount);

            var rrhhProfile = new RRHHProfile(new CreateRRHHProfileCommand(
                    command.RRHHDepartment(),
                    command.statusHierarchy(),
                    userAccount.getId()
            ));

            rrhhProfileRepository.save(rrhhProfile);
            return Optional.of(rrhhProfile);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error during RRHH sign up: %s".formatted(e.getMessage()));
        }
    }

    /**
     * Handles the sign-up completion for an RRHH user authenticated through Google.
     * The Google id_token is re-validated to obtain the trusted email; the User, its Google-backed
     * UserAccount (with a random encoded password, since access is delegated to Google) and the
     * RRHHProfile are then created with the real data provided in the completion form.
     * A fresh application access token is returned so the user ends the flow authenticated.
     * @param command the command containing the Google id_token and the RRHH profile data
     * @return an Optional containing the created UserAccount and the application access token
     */
    @Transactional
    @Override
    public Optional<ImmutablePair<UserAccount, String>> handle(GoogleRRHHSignUpCommand command) {
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

            var rrhhProfile = new RRHHProfile(new CreateRRHHProfileCommand(
                    command.RRHHDepartment(),
                    command.statusHierarchy(),
                    userAccount.getId()
            ));
            rrhhProfileRepository.save(rrhhProfile);

            var token = tokenService.generateToken(userAccount.getEmail());
            return Optional.of(ImmutablePair.of(userAccount, token));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error during Google RRHH sign up: %s".formatted(e.getMessage()));
        }
    }

}
