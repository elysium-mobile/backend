package pe.edu.upc.soft.work.platform.iam.application.internal.commandservices;

import jakarta.transaction.Transactional;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.acl.ExternalDashboardServiceFromIAM;
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

@Service
public class EmployeeProfileCommandServiceImpl implements EmployeeProfileCommandService {

    private final EmployeeProfileRepository employeeProfileRepository;
    private final UserAccountRepository userAccountRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final ExternalDashboardServiceFromIAM externalDashboardServiceFromIAM;

    public EmployeeProfileCommandServiceImpl(EmployeeProfileRepository employeeProfileRepository,
                                             UserAccountRepository userAccountRepository,
                                             HashingService hashingService,
                                             TokenService tokenService,
                                             UserRepository userRepository,
                                             ExternalDashboardServiceFromIAM externalDashboardServiceFromIAM) {
        this.employeeProfileRepository = employeeProfileRepository;
        this.userAccountRepository = userAccountRepository;
        this.hashingService = hashingService;
        this.tokenService=tokenService;
        this.userRepository = userRepository;
        this.externalDashboardServiceFromIAM= externalDashboardServiceFromIAM;
    }

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

    @Override
    public Optional<EmployeeProfile> handle(UpdateEmployeeProfileCommand command) {
        var employeeId = command.employeeProfileId();
        var employeeToUpdate = this.employeeProfileRepository.findById(employeeId).get();
        employeeToUpdate.updateEmployeeProfile(command);
        try {
            var updatedEmployeeProfile = employeeProfileRepository.save(employeeToUpdate);
            return Optional.of(updatedEmployeeProfile);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while updating employee profile: %s".formatted(e.getMessage()));
        }
    }

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
                    command.anonymousName(),
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

//    @Transactional
//    @Override
//    public Optional<ImmutablePair<UserAccount, String>> handle(SignInCommand command) {
//        var userAccount = userAccountRepository.findByEmail(command.email());
//
//        if (userAccount.isEmpty()){
//            throw new IllegalArgumentException("[UserAccountCommandServiceImpl] User Account not found");
//        }
//
//        if(!hashingService.matches(command.password(), userAccount.get().getPassword())){
//            throw new IllegalArgumentException("[UserAccountCommandServiceImpl] Invalid password");
//        }
//
//        var token = tokenService.generateToken(userAccount.get().getEmail());
//        return Optional.of(ImmutablePair.of(userAccount.get(),token));
//    }

}
