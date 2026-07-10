package pe.edu.upc.soft.work.platform.iam.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.apache.commons.lang3.tuple.ImmutablePair;
import pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.acl.ExternalDashboardServiceFromIAM;
import pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.google.GoogleTokenService;
import pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.google.GoogleUserInfo;
import pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.hashing.HashingService;
import pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.tokens.TokenService;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.User;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.DeleteEmployeeProfileCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.UpdateEmployeeProfileCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.entities.EmployeeProfile;
import pe.edu.upc.soft.work.platform.iam.domain.model.valueobjects.WorkOfTeamId;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.EmployeeProfileRepository;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.UserAccountRepository;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import pe.edu.upc.soft.work.platform.iam.test.fixtures.IamCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;
import pe.edu.upc.soft.work.platform.shared.test.fixtures.CommonCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.fixtures.UserInputFixture;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import java.util.Date;
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
class EmployeeProfileCommandServiceImplTest {

    private static final Long USER_ACCOUNT_ID = 10L;
    private static final Long WORK_OF_TEAM_ID = 3L;

    @Mock
    private EmployeeProfileRepository employeeProfileRepository;
    @Mock
    private UserAccountRepository userAccountRepository;
    @Mock
    private HashingService hashingService;
    @Mock
    private TokenService tokenService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ExternalDashboardServiceFromIAM externalDashboardServiceFromIAM;
    @Mock
    private GoogleTokenService googleTokenService;

    @InjectMocks
    private EmployeeProfileCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateEmployeeProfileCommand) -> creates profile and returns id when account and work team exist (AAA)")
    void handleCreateProfileSuccess() {
        // Arrange
        var command = IamCommandFixtures.validCreateEmployeeProfileCommand(USER_ACCOUNT_ID, WORK_OF_TEAM_ID);
        when(userAccountRepository.existsById(USER_ACCOUNT_ID)).thenReturn(true);
        when(externalDashboardServiceFromIAM.existsWorkTeamById(WORK_OF_TEAM_ID)).thenReturn(true);
        when(employeeProfileRepository.save(any(EmployeeProfile.class))).thenAnswer(inv -> {
            EmployeeProfile saved = inv.getArgument(0);
            ReflectionTestUtils.setId(saved, 77L);
            return saved;
        });

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(77L);
        verify(userAccountRepository).existsById(USER_ACCOUNT_ID);
        verify(externalDashboardServiceFromIAM).existsWorkTeamById(WORK_OF_TEAM_ID);
        verify(employeeProfileRepository).save(any(EmployeeProfile.class));
        verifyNoMoreInteractions(employeeProfileRepository, userAccountRepository, externalDashboardServiceFromIAM);
        verifyNoInteractions(hashingService, tokenService, userRepository);
    }

    @Test
    @DisplayName("handle(CreateEmployeeProfileCommand) -> throws NotFoundArgumentException when user account is missing (AAA)")
    void handleCreateProfileMissingUserAccount() {
        // Arrange
        var command = IamCommandFixtures.validCreateEmployeeProfileCommand(USER_ACCOUNT_ID, WORK_OF_TEAM_ID);
        when(userAccountRepository.existsById(USER_ACCOUNT_ID)).thenReturn(false);

        // Act + Assert
        NotFoundArgumentException ex = assertThrows(NotFoundArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("User Account ID: " + USER_ACCOUNT_ID);
        verify(userAccountRepository).existsById(USER_ACCOUNT_ID);
        verifyNoMoreInteractions(userAccountRepository);
        verifyNoInteractions(employeeProfileRepository, externalDashboardServiceFromIAM,
                hashingService, tokenService, userRepository);
    }

    @Test
    @DisplayName("handle(CreateEmployeeProfileCommand) -> throws NotFoundArgumentException when work team is missing (AAA)")
    void handleCreateProfileMissingWorkTeam() {
        // Arrange
        var command = IamCommandFixtures.validCreateEmployeeProfileCommand(USER_ACCOUNT_ID, WORK_OF_TEAM_ID);
        when(userAccountRepository.existsById(USER_ACCOUNT_ID)).thenReturn(true);
        when(externalDashboardServiceFromIAM.existsWorkTeamById(WORK_OF_TEAM_ID)).thenReturn(false);

        // Act + Assert
        NotFoundArgumentException ex = assertThrows(NotFoundArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Work Of Team ID: " + WORK_OF_TEAM_ID);
        verify(userAccountRepository).existsById(USER_ACCOUNT_ID);
        verify(externalDashboardServiceFromIAM).existsWorkTeamById(WORK_OF_TEAM_ID);
        verifyNoMoreInteractions(userAccountRepository, externalDashboardServiceFromIAM);
        verifyNoInteractions(employeeProfileRepository, hashingService, tokenService, userRepository);
    }

    @Test
    @DisplayName("handle(CreateEmployeeProfileCommand) -> wraps save failure in IllegalArgumentException (AAA)")
    void handleCreateProfileSaveFailure() {
        // Arrange
        var command = IamCommandFixtures.validCreateEmployeeProfileCommand(USER_ACCOUNT_ID, WORK_OF_TEAM_ID);
        when(userAccountRepository.existsById(USER_ACCOUNT_ID)).thenReturn(true);
        when(externalDashboardServiceFromIAM.existsWorkTeamById(WORK_OF_TEAM_ID)).thenReturn(true);
        when(employeeProfileRepository.save(any(EmployeeProfile.class)))
                .thenThrow(new RuntimeException("db down"));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error saving employee profile").contains("db down");
        verify(userAccountRepository).existsById(USER_ACCOUNT_ID);
        verify(externalDashboardServiceFromIAM).existsWorkTeamById(WORK_OF_TEAM_ID);
        verify(employeeProfileRepository).save(any(EmployeeProfile.class));
        verifyNoMoreInteractions(employeeProfileRepository, userAccountRepository, externalDashboardServiceFromIAM);
    }

    @Test
    @DisplayName("handle(UpdateEmployeeProfileCommand) -> returns Optional with updated profile when present (AAA)")
    void handleUpdateProfileSuccess() {
        // Arrange
        var existing = new EmployeeProfile(
                IamCommandFixtures.validCreateEmployeeProfileCommand(USER_ACCOUNT_ID, WORK_OF_TEAM_ID));
        ReflectionTestUtils.setId(existing, 55L);
        var command = new UpdateEmployeeProfileCommand(55L, new Date(1L), "Senior", 9000, new WorkOfTeamId(WORK_OF_TEAM_ID));
        when(employeeProfileRepository.findById(55L)).thenReturn(Optional.of(existing));
        when(employeeProfileRepository.save(existing)).thenReturn(existing);

        // Act
        Optional<EmployeeProfile> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getPosition()).isEqualTo("Senior");
        assertThat(result.get().getSalary()).isEqualTo(9000);
        verify(employeeProfileRepository).findById(55L);
        verify(employeeProfileRepository).save(existing);
        verifyNoMoreInteractions(employeeProfileRepository);
        verifyNoInteractions(userAccountRepository, externalDashboardServiceFromIAM,
                hashingService, tokenService, userRepository);
    }

    @Test
    @DisplayName("handle(UpdateEmployeeProfileCommand) -> wraps save failure in IllegalArgumentException (AAA)")
    void handleUpdateProfileSaveFailure() {
        // Arrange
        var existing = new EmployeeProfile(
                IamCommandFixtures.validCreateEmployeeProfileCommand(USER_ACCOUNT_ID, WORK_OF_TEAM_ID));
        ReflectionTestUtils.setId(existing, 55L);
        var command = new UpdateEmployeeProfileCommand(55L, new Date(1L), "Senior", 9000, new WorkOfTeamId(WORK_OF_TEAM_ID));
        when(employeeProfileRepository.findById(55L)).thenReturn(Optional.of(existing));
        when(employeeProfileRepository.save(existing)).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error while updating employee profile").contains("boom");
        verify(employeeProfileRepository).findById(55L);
        verify(employeeProfileRepository).save(existing);
        verifyNoMoreInteractions(employeeProfileRepository);
    }

    @Test
    @DisplayName("handle(DeleteEmployeeProfileCommand) -> deletes when present (AAA)")
    void handleDeleteProfileSuccess() {
        // Arrange
        var command = new DeleteEmployeeProfileCommand(88L);
        when(employeeProfileRepository.existsById(88L)).thenReturn(true);

        // Act
        service.handle(command);

        // Assert
        verify(employeeProfileRepository).existsById(88L);
        verify(employeeProfileRepository).deleteById(88L);
        verifyNoMoreInteractions(employeeProfileRepository);
    }

    @Test
    @DisplayName("handle(DeleteEmployeeProfileCommand) -> throws IllegalArgumentException when id is absent (AAA)")
    void handleDeleteProfileMissing() {
        // Arrange
        var command = new DeleteEmployeeProfileCommand(88L);
        when(employeeProfileRepository.existsById(88L)).thenReturn(false);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Employee profile with id 88 not found");
        verify(employeeProfileRepository).existsById(88L);
        verify(employeeProfileRepository, never()).deleteById(any(Long.class));
        verifyNoMoreInteractions(employeeProfileRepository);
    }

    @Test
    @DisplayName("handle(DeleteEmployeeProfileCommand) -> wraps deleteById failure in IllegalArgumentException (AAA)")
    void handleDeleteProfileDeleteFailure() {
        // Arrange
        var command = new DeleteEmployeeProfileCommand(88L);
        when(employeeProfileRepository.existsById(88L)).thenReturn(true);
        org.mockito.Mockito.doThrow(new RuntimeException("fk")).when(employeeProfileRepository).deleteById(88L);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error while deleting employee profile").contains("fk");
        verify(employeeProfileRepository).existsById(88L);
        verify(employeeProfileRepository).deleteById(88L);
        verifyNoMoreInteractions(employeeProfileRepository);
    }

    @Test
    @DisplayName("handle(EmployeeSignUpCommand) -> creates User, UserAccount and EmployeeProfile when email is unused (AAA)")
    void handleSignUpSuccess() {
        // Arrange
        var command = IamCommandFixtures.employeeSignUpCommandFrom(UserInputFixture.valid());
        when(userAccountRepository.existsByEmail(CommonCommandFixtures.VALID_EMAIL)).thenReturn(false);
        when(hashingService.encode(CommonCommandFixtures.VALID_PASSWORD)).thenReturn("hashed-secret");
        ArgumentCaptor<EmployeeProfile> profileCaptor = ArgumentCaptor.forClass(EmployeeProfile.class);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            ReflectionTestUtils.setId(u, 1L);
            return u;
        });
        when(userAccountRepository.save(any(UserAccount.class))).thenAnswer(inv -> {
            UserAccount ua = inv.getArgument(0);
            ReflectionTestUtils.setId(ua, 2L);
            return ua;
        });
        when(employeeProfileRepository.save(profileCaptor.capture())).thenAnswer(inv -> {
            EmployeeProfile p = inv.getArgument(0);
            ReflectionTestUtils.setId(p, 3L);
            return p;
        });

        // Act
        Optional<EmployeeProfile> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(profileCaptor.getValue().getPosition()).isEqualTo("Engineer");
        assertThat(profileCaptor.getValue().getSalary()).isEqualTo(5000);
        assertThat(profileCaptor.getValue().getUserAccountId()).isEqualTo(2L);
        verify(userAccountRepository).existsByEmail(CommonCommandFixtures.VALID_EMAIL);
        verify(hashingService).encode(CommonCommandFixtures.VALID_PASSWORD);
        verify(userRepository).save(any(User.class));
        verify(userAccountRepository).save(any(UserAccount.class));
        verify(employeeProfileRepository).save(any(EmployeeProfile.class));
        verifyNoMoreInteractions(userRepository, userAccountRepository, hashingService, employeeProfileRepository);
        verifyNoInteractions(tokenService, externalDashboardServiceFromIAM);
    }

    @Test
    @DisplayName("handle(EmployeeSignUpCommand) -> throws IllegalArgumentException when email already exists (AAA)")
    void handleSignUpEmailExists() {
        // Arrange
        var command = IamCommandFixtures.employeeSignUpCommandFrom(UserInputFixture.valid());
        when(userAccountRepository.existsByEmail(CommonCommandFixtures.VALID_EMAIL)).thenReturn(true);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Email already exists");
        verify(userAccountRepository).existsByEmail(CommonCommandFixtures.VALID_EMAIL);
        verifyNoMoreInteractions(userAccountRepository);
        verifyNoInteractions(userRepository, hashingService, employeeProfileRepository,
                tokenService, externalDashboardServiceFromIAM);
    }

    @Test
    @DisplayName("handle(EmployeeSignUpCommand) -> wraps downstream save failure in IllegalArgumentException (AAA)")
    void handleSignUpSaveFailure() {
        // Arrange
        var command = IamCommandFixtures.employeeSignUpCommandFrom(UserInputFixture.valid());
        when(userAccountRepository.existsByEmail(CommonCommandFixtures.VALID_EMAIL)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("oops"));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error during sign up").contains("oops");
        verify(userAccountRepository).existsByEmail(CommonCommandFixtures.VALID_EMAIL);
        verify(userRepository).save(any(User.class));
        verifyNoMoreInteractions(userAccountRepository, userRepository);
    }

    @Test
    @DisplayName("handle(GoogleEmployeeSignUpCommand) -> creates User, UserAccount, EmployeeProfile and returns token (AAA)")
    void handleGoogleSignUpSuccess() {
        // Arrange
        var command = IamCommandFixtures.googleEmployeeSignUpCommandFrom(UserInputFixture.valid());
        var googleUserInfo = new GoogleUserInfo(
                "1234567890", CommonCommandFixtures.VALID_EMAIL,
                CommonCommandFixtures.VALID_NAME, CommonCommandFixtures.VALID_LAST_NAME);
        when(googleTokenService.verify(command.idToken())).thenReturn(googleUserInfo);
        when(userAccountRepository.existsByEmail(CommonCommandFixtures.VALID_EMAIL)).thenReturn(false);
        when(hashingService.encode(any(CharSequence.class))).thenReturn("hashed-random");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            ReflectionTestUtils.setId(u, 1L);
            return u;
        });
        when(userAccountRepository.save(any(UserAccount.class))).thenAnswer(inv -> {
            UserAccount ua = inv.getArgument(0);
            ReflectionTestUtils.setId(ua, 2L);
            return ua;
        });
        when(employeeProfileRepository.save(any(EmployeeProfile.class))).thenAnswer(inv -> inv.getArgument(0));
        when(tokenService.generateToken(CommonCommandFixtures.VALID_EMAIL)).thenReturn("token-google");

        // Act
        Optional<ImmutablePair<UserAccount, String>> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getLeft().getEmail()).isEqualTo(CommonCommandFixtures.VALID_EMAIL);
        assertThat(result.get().getRight()).isEqualTo("token-google");
        verify(googleTokenService).verify(command.idToken());
        verify(userAccountRepository).existsByEmail(CommonCommandFixtures.VALID_EMAIL);
        verify(userRepository).save(any(User.class));
        verify(userAccountRepository).save(any(UserAccount.class));
        verify(employeeProfileRepository).save(any(EmployeeProfile.class));
        verify(tokenService).generateToken(CommonCommandFixtures.VALID_EMAIL);
        verifyNoInteractions(externalDashboardServiceFromIAM);
    }

    @Test
    @DisplayName("handle(GoogleEmployeeSignUpCommand) -> throws IllegalArgumentException when email already exists (AAA)")
    void handleGoogleSignUpEmailExists() {
        // Arrange
        var command = IamCommandFixtures.googleEmployeeSignUpCommandFrom(UserInputFixture.valid());
        var googleUserInfo = new GoogleUserInfo(
                "1234567890", CommonCommandFixtures.VALID_EMAIL,
                CommonCommandFixtures.VALID_NAME, CommonCommandFixtures.VALID_LAST_NAME);
        when(googleTokenService.verify(command.idToken())).thenReturn(googleUserInfo);
        when(userAccountRepository.existsByEmail(CommonCommandFixtures.VALID_EMAIL)).thenReturn(true);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Email already exists");
        verify(googleTokenService).verify(command.idToken());
        verify(userAccountRepository).existsByEmail(CommonCommandFixtures.VALID_EMAIL);
        verifyNoMoreInteractions(userAccountRepository);
        verifyNoInteractions(userRepository, hashingService, employeeProfileRepository,
                tokenService, externalDashboardServiceFromIAM);
    }
}
