package pe.edu.upc.soft.work.platform.iam.application.internal.commandservices;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.google.GoogleTokenService;
import pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.google.GoogleUserInfo;
import pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.hashing.HashingService;
import pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.tokens.TokenService;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.User;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.DeleteUserAccountCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.GoogleSignInCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.SignInCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.UpdateUserAccountCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.valueobjects.CompanyId;
import pe.edu.upc.soft.work.platform.iam.domain.model.valueobjects.MembershipId;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.EmployeeProfileRepository;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.RRHHProfileRepository;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.UserAccountRepository;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import pe.edu.upc.soft.work.platform.iam.test.fixtures.IamCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.fixtures.CommonCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.fixtures.UserInputFixture;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAccountCommandServiceImplTest {

    private static final Long USER_ID = 1L;
    private static final Long ACCOUNT_ID = 70L;

    @Mock
    private UserAccountRepository userAccountRepository;
    @Mock
    private EmployeeProfileRepository employeeProfileRepository;
    @Mock
    private RRHHProfileRepository rrhhProfileRepository;
    @Mock
    private HashingService hashingService;
    @Mock
    private TokenService tokenService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private GoogleTokenService googleTokenService;

    @InjectMocks
    private UserAccountCommandServiceImpl service;

    private static UpdateUserAccountCommand validUpdate(Long accountId, String password) {
        return new UpdateUserAccountCommand(
                accountId,
                CommonCommandFixtures.VALID_EMAIL,
                password,
                CommonCommandFixtures.VALID_ANONYMOUS_NAME,
                new MembershipId(0L),
                new CompanyId(0L));
    }

    @Test
    @DisplayName("handle(CreateUserAccountCommand) -> creates account and returns generated id (AAA)")
    void handleCreateSuccess() {
        // Arrange
        var command = IamCommandFixtures.createUserAccountCommandFrom(USER_ID, UserInputFixture.valid());
        when(userAccountRepository.findByEmail(command.email())).thenReturn(Optional.empty());
        when(userAccountRepository.save(any(UserAccount.class))).thenAnswer(inv -> {
            UserAccount ua = inv.getArgument(0);
            ReflectionTestUtils.setId(ua, ACCOUNT_ID);
            ReflectionTestUtils.setField(ua, "userId", USER_ID);
            return ua;
        });

        // Act
        Long id = service.handle(command);

        // Assert
        assertThat(id).isEqualTo(ACCOUNT_ID);
        verify(userAccountRepository).findByEmail(command.email());
        verify(userAccountRepository).save(any(UserAccount.class));
        verifyNoMoreInteractions(userAccountRepository);
        verifyNoInteractions(employeeProfileRepository, rrhhProfileRepository,
            hashingService, tokenService, userRepository);
    }

    @Test
    @DisplayName("handle(CreateUserAccountCommand) -> wraps save failure in IllegalArgumentException (AAA)")
    void handleCreateSaveFailure() {
        // Arrange
        var command = IamCommandFixtures.createUserAccountCommandFrom(USER_ID, UserInputFixture.valid());
        when(userAccountRepository.findByEmail(command.email())).thenReturn(Optional.empty());
        when(userAccountRepository.save(any(UserAccount.class))).thenThrow(new RuntimeException("db"));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error saving user account").contains("db");
        verify(userAccountRepository).findByEmail(command.email());
        verify(userAccountRepository).save(any(UserAccount.class));
        verifyNoMoreInteractions(userAccountRepository);
    }

    @Test
    @DisplayName("handle(UpdateUserAccountCommand) -> returns Optional with updated account (AAA)")
    void handleUpdateSuccess() {
        // Arrange
        var existing = new UserAccount(
                IamCommandFixtures.createUserAccountCommandFrom(USER_ID, UserInputFixture.valid()));
        ReflectionTestUtils.setId(existing, ACCOUNT_ID);
        var command = validUpdate(ACCOUNT_ID, "newSecret");
        when(userAccountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(existing));
        when(userAccountRepository.save(existing)).thenReturn(existing);

        // Act
        Optional<UserAccount> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getPassword()).isEqualTo("newSecret");
        verify(userAccountRepository).findById(ACCOUNT_ID);
        verify(userAccountRepository).save(existing);
        verifyNoMoreInteractions(userAccountRepository);
    }

    @Test
    @DisplayName("handle(UpdateUserAccountCommand) -> wraps save failure in IllegalArgumentException (AAA)")
    void handleUpdateSaveFailure() {
        // Arrange
        var existing = new UserAccount(
                IamCommandFixtures.createUserAccountCommandFrom(USER_ID, UserInputFixture.valid()));
        ReflectionTestUtils.setId(existing, ACCOUNT_ID);
        var command = validUpdate(ACCOUNT_ID, "newSecret");
        when(userAccountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(existing));
        when(userAccountRepository.save(existing)).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error while updating user account").contains("boom");
        verify(userAccountRepository).findById(ACCOUNT_ID);
        verify(userAccountRepository).save(existing);
        verifyNoMoreInteractions(userAccountRepository);
    }

    @Test
    @DisplayName("handle(DeleteUserAccountCommand) -> deletes when present (AAA)")
    void handleDeleteSuccess() {
        // Arrange
        var command = new DeleteUserAccountCommand(ACCOUNT_ID);
        when(userAccountRepository.existsById(ACCOUNT_ID)).thenReturn(true);

        // Act
        service.handle(command);

        // Assert
        verify(userAccountRepository).existsById(ACCOUNT_ID);
        verify(userAccountRepository).deleteById(ACCOUNT_ID);
        verifyNoMoreInteractions(userAccountRepository);
    }

    @Test
    @DisplayName("handle(DeleteUserAccountCommand) -> throws IllegalArgumentException when id is absent (AAA)")
    void handleDeleteMissing() {
        // Arrange
        var command = new DeleteUserAccountCommand(ACCOUNT_ID);
        when(userAccountRepository.existsById(ACCOUNT_ID)).thenReturn(false);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("User account with id " + ACCOUNT_ID + " not found");
        verify(userAccountRepository).existsById(ACCOUNT_ID);
        verify(userAccountRepository, never()).deleteById(any(Long.class));
        verifyNoMoreInteractions(userAccountRepository);
    }

    @Test
    @DisplayName("handle(DeleteUserAccountCommand) -> wraps deleteById failure in IllegalArgumentException (AAA)")
    void handleDeleteDeleteFailure() {
        // Arrange
        var command = new DeleteUserAccountCommand(ACCOUNT_ID);
        when(userAccountRepository.existsById(ACCOUNT_ID)).thenReturn(true);
        org.mockito.Mockito.doThrow(new RuntimeException("fk")).when(userAccountRepository).deleteById(ACCOUNT_ID);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error while deleting user account").contains("fk");
        verify(userAccountRepository).existsById(ACCOUNT_ID);
        verify(userAccountRepository).deleteById(ACCOUNT_ID);
        verifyNoMoreInteractions(userAccountRepository);
    }

    @Test
    @DisplayName("handle(SignInCommand) -> returns pair of (account,token) when credentials match (AAA)")
    void handleSignInSuccess() {
        // Arrange
        var account = new UserAccount(
                IamCommandFixtures.createUserAccountCommandFrom(USER_ID, UserInputFixture.valid()));
        ReflectionTestUtils.setId(account, ACCOUNT_ID);
        var command = IamCommandFixtures.signInCommandFrom(UserInputFixture.valid());
        when(userAccountRepository.findByEmail(CommonCommandFixtures.VALID_EMAIL)).thenReturn(Optional.of(account));
        when(hashingService.matches(CommonCommandFixtures.VALID_PASSWORD, account.getPassword())).thenReturn(true);
        when(tokenService.generateToken(CommonCommandFixtures.VALID_EMAIL)).thenReturn("token-abc");

        // Act
        Optional<ImmutablePair<UserAccount, String>> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getLeft()).isSameAs(account);
        assertThat(result.get().getRight()).isEqualTo("token-abc");
        verify(userAccountRepository).findByEmail(CommonCommandFixtures.VALID_EMAIL);
        verify(hashingService).matches(CommonCommandFixtures.VALID_PASSWORD, CommonCommandFixtures.VALID_PASSWORD);
        verify(tokenService).generateToken(CommonCommandFixtures.VALID_EMAIL);
        verifyNoMoreInteractions(userAccountRepository, hashingService, tokenService);
        verifyNoInteractions(employeeProfileRepository, rrhhProfileRepository, userRepository);
    }

    @Test
    @DisplayName("handle(SignInCommand) -> throws IllegalArgumentException when user account is not found (AAA)")
    void handleSignInUserNotFound() {
        // Arrange
        var command = IamCommandFixtures.signInCommandFrom(UserInputFixture.valid());
        when(userAccountRepository.findByEmail(CommonCommandFixtures.VALID_EMAIL)).thenReturn(Optional.empty());

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("User Account not found");
        verify(userAccountRepository).findByEmail(CommonCommandFixtures.VALID_EMAIL);
        verifyNoMoreInteractions(userAccountRepository);
        verifyNoInteractions(hashingService, tokenService, employeeProfileRepository, rrhhProfileRepository, userRepository);
    }

    @Test
    @DisplayName("handle(SignInCommand) -> throws IllegalArgumentException when password does not match (AAA)")
    void handleSignInInvalidPassword() {
        // Arrange
        var account = new UserAccount(
                IamCommandFixtures.createUserAccountCommandFrom(USER_ID, UserInputFixture.valid()));
        ReflectionTestUtils.setId(account, ACCOUNT_ID);
        var command = new SignInCommand(CommonCommandFixtures.VALID_EMAIL, "wrong");
        when(userAccountRepository.findByEmail(CommonCommandFixtures.VALID_EMAIL)).thenReturn(Optional.of(account));
        when(hashingService.matches("wrong", account.getPassword())).thenReturn(false);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Invalid password");
        verify(userAccountRepository).findByEmail(CommonCommandFixtures.VALID_EMAIL);
        verify(hashingService).matches("wrong", CommonCommandFixtures.VALID_PASSWORD);
        verifyNoMoreInteractions(userAccountRepository, hashingService);
        verifyNoInteractions(tokenService, employeeProfileRepository, rrhhProfileRepository, userRepository);
    }

    @Test
    @DisplayName("handle(GoogleSignInCommand) -> returns pair of (account,token) when email already exists (AAA)")
    void handleGoogleSignInExistingAccount() {
        // Arrange
        var account = new UserAccount(
                IamCommandFixtures.createUserAccountCommandFrom(USER_ID, UserInputFixture.valid()));
        ReflectionTestUtils.setId(account, ACCOUNT_ID);
        var command = IamCommandFixtures.googleSignInCommand();
        var googleUserInfo = new GoogleUserInfo(
                "1234567890", CommonCommandFixtures.VALID_EMAIL,
                CommonCommandFixtures.VALID_NAME, CommonCommandFixtures.VALID_LAST_NAME);
        when(googleTokenService.verify(command.idToken())).thenReturn(googleUserInfo);
        when(userAccountRepository.findByEmail(CommonCommandFixtures.VALID_EMAIL)).thenReturn(Optional.of(account));
        when(tokenService.generateToken(CommonCommandFixtures.VALID_EMAIL)).thenReturn("token-google");

        // Act
        Optional<ImmutablePair<UserAccount, String>> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getLeft()).isSameAs(account);
        assertThat(result.get().getRight()).isEqualTo("token-google");
        verify(googleTokenService).verify(command.idToken());
        verify(userAccountRepository).findByEmail(CommonCommandFixtures.VALID_EMAIL);
        verify(tokenService).generateToken(CommonCommandFixtures.VALID_EMAIL);
        verifyNoMoreInteractions(userAccountRepository, tokenService);
        verifyNoInteractions(userRepository, hashingService, employeeProfileRepository, rrhhProfileRepository);
    }

    @Test
    @DisplayName("handle(GoogleSignInCommand) -> provisions User and UserAccount when email is unknown (AAA)")
    void handleGoogleSignInNewAccount() {
        // Arrange
        var command = IamCommandFixtures.googleSignInCommand();
        var googleUserInfo = new GoogleUserInfo(
                "1234567890", "new.user@gmail.com", "New", "User");
        when(googleTokenService.verify(command.idToken())).thenReturn(googleUserInfo);
        when(userAccountRepository.findByEmail("new.user@gmail.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User user = inv.getArgument(0);
            ReflectionTestUtils.setId(user, USER_ID);
            return user;
        });
        when(hashingService.encode(any(CharSequence.class))).thenReturn("hashed-random");
        when(userAccountRepository.save(any(UserAccount.class))).thenAnswer(inv -> inv.getArgument(0));
        when(tokenService.generateToken("new.user@gmail.com")).thenReturn("token-new");

        // Act
        Optional<ImmutablePair<UserAccount, String>> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getLeft().getEmail()).isEqualTo("new.user@gmail.com");
        assertThat(result.get().getLeft().getUserId()).isEqualTo(USER_ID);
        assertThat(result.get().getRight()).isEqualTo("token-new");
        verify(googleTokenService).verify(command.idToken());
        verify(userRepository).save(any(User.class));
        verify(userAccountRepository).save(any(UserAccount.class));
        verify(tokenService).generateToken("new.user@gmail.com");
    }

    @Test
    @DisplayName("handle(GoogleSignInCommand) -> propagates IllegalArgumentException when token is invalid (AAA)")
    void handleGoogleSignInInvalidToken() {
        // Arrange
        var command = IamCommandFixtures.googleSignInCommand();
        when(googleTokenService.verify(command.idToken()))
                .thenThrow(new IllegalArgumentException("Invalid Google id_token"));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Invalid Google id_token");
        verify(googleTokenService).verify(command.idToken());
        verifyNoInteractions(userAccountRepository, userRepository, hashingService, tokenService,
                employeeProfileRepository, rrhhProfileRepository);
    }
}
