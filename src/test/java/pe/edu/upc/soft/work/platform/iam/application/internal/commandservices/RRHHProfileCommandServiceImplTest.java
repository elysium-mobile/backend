package pe.edu.upc.soft.work.platform.iam.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.hashing.HashingService;
import pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.tokens.TokenService;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.User;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.DeleteRRHHProfileCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.UpdateRRHHProfileCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.entities.RRHHProfile;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.RRHHProfileRepository;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.UserAccountRepository;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import pe.edu.upc.soft.work.platform.iam.test.fixtures.IamCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;
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
class RRHHProfileCommandServiceImplTest {

    private static final Long USER_ACCOUNT_ID = 10L;

    @Mock
    private RRHHProfileRepository rrhhProfileRepository;
    @Mock
    private UserAccountRepository userAccountRepository;
    @Mock
    private HashingService hashingService;
    @Mock
    private TokenService tokenService;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RRHHProfileCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateRRHHProfileCommand) -> creates profile and returns id when user account exists (AAA)")
    void handleCreateSuccess() {
        // Arrange
        var command = IamCommandFixtures.validCreateRRHHProfileCommand(USER_ACCOUNT_ID);
        when(userAccountRepository.existsById(USER_ACCOUNT_ID)).thenReturn(true);
        when(rrhhProfileRepository.save(any(RRHHProfile.class))).thenAnswer(inv -> {
            RRHHProfile p = inv.getArgument(0);
            ReflectionTestUtils.setId(p, 99L);
            return p;
        });

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(99L);
        verify(userAccountRepository).existsById(USER_ACCOUNT_ID);
        verify(rrhhProfileRepository).save(any(RRHHProfile.class));
        verifyNoMoreInteractions(userAccountRepository, rrhhProfileRepository);
        verifyNoInteractions(hashingService, tokenService, userRepository);
    }

    @Test
    @DisplayName("handle(CreateRRHHProfileCommand) -> throws NotFoundArgumentException when user account is missing (AAA)")
    void handleCreateMissingUserAccount() {
        // Arrange
        var command = IamCommandFixtures.validCreateRRHHProfileCommand(USER_ACCOUNT_ID);
        when(userAccountRepository.existsById(USER_ACCOUNT_ID)).thenReturn(false);

        // Act + Assert
        NotFoundArgumentException ex = assertThrows(NotFoundArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("User Account ID: " + USER_ACCOUNT_ID);
        verify(userAccountRepository).existsById(USER_ACCOUNT_ID);
        verifyNoMoreInteractions(userAccountRepository);
        verifyNoInteractions(rrhhProfileRepository, hashingService, tokenService, userRepository);
    }

    @Test
    @DisplayName("handle(CreateRRHHProfileCommand) -> wraps save failure in IllegalArgumentException (AAA)")
    void handleCreateSaveFailure() {
        // Arrange
        var command = IamCommandFixtures.validCreateRRHHProfileCommand(USER_ACCOUNT_ID);
        when(userAccountRepository.existsById(USER_ACCOUNT_ID)).thenReturn(true);
        when(rrhhProfileRepository.save(any(RRHHProfile.class))).thenThrow(new RuntimeException("db"));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error saving RRHH profile").contains("db");
        verify(userAccountRepository).existsById(USER_ACCOUNT_ID);
        verify(rrhhProfileRepository).save(any(RRHHProfile.class));
        verifyNoMoreInteractions(userAccountRepository, rrhhProfileRepository);
    }

    @Test
    @DisplayName("handle(UpdateRRHHProfileCommand) -> returns Optional with updated profile when present (AAA)")
    void handleUpdateSuccess() {
        // Arrange
        var existing = new RRHHProfile(IamCommandFixtures.validCreateRRHHProfileCommand(USER_ACCOUNT_ID));
        ReflectionTestUtils.setId(existing, 21L);
        var command = new UpdateRRHHProfileCommand(21L, "Payroll", "Junior");
        when(rrhhProfileRepository.findById(21L)).thenReturn(Optional.of(existing));
        when(rrhhProfileRepository.save(existing)).thenReturn(existing);

        // Act
        Optional<RRHHProfile> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getRRHHDepartment()).isEqualTo("Payroll");
        assertThat(result.get().getStatusHierarchy()).isEqualTo("Junior");
        verify(rrhhProfileRepository).findById(21L);
        verify(rrhhProfileRepository).save(existing);
        verifyNoMoreInteractions(rrhhProfileRepository);
    }

    @Test
    @DisplayName("handle(UpdateRRHHProfileCommand) -> wraps save failure in IllegalArgumentException (AAA)")
    void handleUpdateSaveFailure() {
        // Arrange
        var existing = new RRHHProfile(IamCommandFixtures.validCreateRRHHProfileCommand(USER_ACCOUNT_ID));
        ReflectionTestUtils.setId(existing, 21L);
        var command = new UpdateRRHHProfileCommand(21L, "Payroll", "Junior");
        when(rrhhProfileRepository.findById(21L)).thenReturn(Optional.of(existing));
        when(rrhhProfileRepository.save(existing)).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error while updating RRHH profile").contains("boom");
        verify(rrhhProfileRepository).findById(21L);
        verify(rrhhProfileRepository).save(existing);
        verifyNoMoreInteractions(rrhhProfileRepository);
    }

    @Test
    @DisplayName("handle(DeleteRRHHProfileCommand) -> deletes when present (AAA)")
    void handleDeleteSuccess() {
        // Arrange
        var command = new DeleteRRHHProfileCommand(40L);
        when(rrhhProfileRepository.existsById(40L)).thenReturn(true);

        // Act
        service.handle(command);

        // Assert
        verify(rrhhProfileRepository).existsById(40L);
        verify(rrhhProfileRepository).deleteById(40L);
        verifyNoMoreInteractions(rrhhProfileRepository);
    }

    @Test
    @DisplayName("handle(DeleteRRHHProfileCommand) -> throws IllegalArgumentException when id is absent (AAA)")
    void handleDeleteMissing() {
        // Arrange
        var command = new DeleteRRHHProfileCommand(40L);
        when(rrhhProfileRepository.existsById(40L)).thenReturn(false);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("RRHH profile with id 40 not found");
        verify(rrhhProfileRepository).existsById(40L);
        verify(rrhhProfileRepository, never()).deleteById(any(Long.class));
        verifyNoMoreInteractions(rrhhProfileRepository);
    }

    @Test
    @DisplayName("handle(DeleteRRHHProfileCommand) -> wraps deleteById failure in IllegalArgumentException (AAA)")
    void handleDeleteDeleteFailure() {
        // Arrange
        var command = new DeleteRRHHProfileCommand(40L);
        when(rrhhProfileRepository.existsById(40L)).thenReturn(true);
        org.mockito.Mockito.doThrow(new RuntimeException("fk")).when(rrhhProfileRepository).deleteById(40L);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error while deleting RRHH profile").contains("fk");
        verify(rrhhProfileRepository).existsById(40L);
        verify(rrhhProfileRepository).deleteById(40L);
        verifyNoMoreInteractions(rrhhProfileRepository);
    }

    @Test
    @DisplayName("handle(RRHHSignUpCommand) -> creates User, UserAccount and RRHHProfile when email is unused (AAA)")
    void handleSignUpSuccess() {
        // Arrange
        var command = IamCommandFixtures.rrhhSignUpCommandFrom(UserInputFixture.valid());
        when(userAccountRepository.existsByEmail(CommonCommandFixtures.VALID_EMAIL)).thenReturn(false);
        when(hashingService.encode(CommonCommandFixtures.VALID_PASSWORD)).thenReturn("hashed-secret");
        ArgumentCaptor<RRHHProfile> profileCaptor = ArgumentCaptor.forClass(RRHHProfile.class);
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
        when(rrhhProfileRepository.save(profileCaptor.capture())).thenAnswer(inv -> {
            RRHHProfile p = inv.getArgument(0);
            ReflectionTestUtils.setId(p, 3L);
            return p;
        });

        // Act
        Optional<RRHHProfile> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(profileCaptor.getValue().getRRHHDepartment()).isEqualTo("Recruiting");
        assertThat(profileCaptor.getValue().getUserAccountId()).isEqualTo(2L);
        verify(userAccountRepository).existsByEmail(CommonCommandFixtures.VALID_EMAIL);
        verify(hashingService).encode(CommonCommandFixtures.VALID_PASSWORD);
        verify(userRepository).save(any(User.class));
        verify(userAccountRepository).save(any(UserAccount.class));
        verify(rrhhProfileRepository).save(any(RRHHProfile.class));
        verifyNoMoreInteractions(userRepository, userAccountRepository, hashingService, rrhhProfileRepository);
        verifyNoInteractions(tokenService);
    }

    @Test
    @DisplayName("handle(RRHHSignUpCommand) -> throws IllegalArgumentException when email already exists (AAA)")
    void handleSignUpEmailExists() {
        // Arrange
        var command = IamCommandFixtures.rrhhSignUpCommandFrom(UserInputFixture.valid());
        when(userAccountRepository.existsByEmail(CommonCommandFixtures.VALID_EMAIL)).thenReturn(true);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Email already exists");
        verify(userAccountRepository).existsByEmail(CommonCommandFixtures.VALID_EMAIL);
        verifyNoMoreInteractions(userAccountRepository);
        verifyNoInteractions(userRepository, hashingService, rrhhProfileRepository, tokenService);
    }

    @Test
    @DisplayName("handle(RRHHSignUpCommand) -> wraps downstream save failure in IllegalArgumentException (AAA)")
    void handleSignUpSaveFailure() {
        // Arrange
        var command = IamCommandFixtures.rrhhSignUpCommandFrom(UserInputFixture.valid());
        when(userAccountRepository.existsByEmail(CommonCommandFixtures.VALID_EMAIL)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("oops"));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error during RRHH sign up").contains("oops");
        verify(userAccountRepository).existsByEmail(CommonCommandFixtures.VALID_EMAIL);
        verify(userRepository).save(any(User.class));
        verifyNoMoreInteractions(userAccountRepository, userRepository);
    }
}
