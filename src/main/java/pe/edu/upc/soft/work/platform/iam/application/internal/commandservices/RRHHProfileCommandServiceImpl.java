package pe.edu.upc.soft.work.platform.iam.application.internal.commandservices;

import jakarta.transaction.Transactional;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.hashing.HashingService;
import pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.tokens.TokenService;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.*;
import pe.edu.upc.soft.work.platform.iam.domain.model.entities.RRHHProfile;
import pe.edu.upc.soft.work.platform.iam.domain.services.RRHHProfileCommandService;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.RRHHProfileRepository;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.UserAccountRepository;

import java.util.Optional;

@Service
public class RRHHProfileCommandServiceImpl implements RRHHProfileCommandService {

    private final RRHHProfileRepository rrhhProfileRepository;
    private final UserAccountRepository userAccountRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;

    public RRHHProfileCommandServiceImpl(RRHHProfileRepository rrhhProfileRepository,
                                         UserAccountRepository userAccountRepository,
                                         HashingService hashingService,
                                         TokenService tokenService) {
        this.rrhhProfileRepository = rrhhProfileRepository;
        this.userAccountRepository = userAccountRepository;
        this.hashingService = hashingService;
        this.tokenService=tokenService;
    }

    @Override
    public Long handle(CreateRRHHProfileCommand command) {
        var rrhhProfile = new RRHHProfile(command);
        try {
            rrhhProfileRepository.save(rrhhProfile);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving RRHH profile: %s".formatted(e.getMessage()));
        }
        return rrhhProfile.getId();
    }

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


    @Transactional
    @Override
    public Optional<RRHHProfile> handle(RRHHSignUpCommand command) {
        if (userAccountRepository.existsByEmail(command.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        var userAccount = new UserAccount(new CreateUserAccountCommand(
                2L,
                command.email(),
                hashingService.encode(command.password()),
                command.email()
        ));

        try {
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


    @Transactional
    @Override
    public Optional<ImmutablePair<UserAccount, String>> handle(SignInCommand command) {
        var userAccount = userAccountRepository.findByEmail(command.email());

        if (userAccount.isEmpty()) {
            throw new IllegalArgumentException("User Account not found");
        }

        if (!hashingService.matches(command.password(), userAccount.get().getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        var token = tokenService.generateToken(userAccount.get().getEmail());
        return Optional.of(ImmutablePair.of(userAccount.get(), token));
    }
}
