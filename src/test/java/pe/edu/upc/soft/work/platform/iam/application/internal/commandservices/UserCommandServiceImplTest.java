package pe.edu.upc.soft.work.platform.iam.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.User;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.DeleteUserCommand;
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
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateUserCommand) -> creates user and returns generated id when DNI is unused (AAA)")
    void handleCreateUserCommandCreatesUser() {
        // Arrange
        var command = IamCommandFixtures.validCreateUserCommand();
        when(userRepository.existsByDni(command.dni())).thenReturn(false);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(userCaptor.capture())).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            ReflectionTestUtils.setId(u, 42L);
            return u;
        });

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(42L);
        assertThat(userCaptor.getValue().getName()).isEqualTo(CommonCommandFixtures.VALID_NAME);
        assertThat(userCaptor.getValue().getDni()).isEqualTo(CommonCommandFixtures.VALID_DNI);
        verify(userRepository).existsByDni(CommonCommandFixtures.VALID_DNI);
        verify(userRepository).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("handle(CreateUserCommand) -> throws RuntimeException when DNI already exists (AAA)")
    void handleCreateUserCommandThrowsWhenDniExists() {
        // Arrange
        var command = IamCommandFixtures.validCreateUserCommand();
        when(userRepository.existsByDni(command.dni())).thenReturn(true);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(CommonCommandFixtures.VALID_DNI).contains("already exists");
        verify(userRepository).existsByDni(CommonCommandFixtures.VALID_DNI);
        verify(userRepository, never()).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("handle(CreateUserCommand) -> wraps repository save failure in RuntimeException (AAA)")
    void handleCreateUserCommandWrapsSaveFailure() {
        // Arrange
        var command = IamCommandFixtures.validCreateUserCommand();
        when(userRepository.existsByDni(command.dni())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("db down"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error creating user").contains("db down");
        verify(userRepository).existsByDni(CommonCommandFixtures.VALID_DNI);
        verify(userRepository).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("handle(UpdateUserCommand) -> returns Optional with updated user when id exists (AAA)")
    void handleUpdateUserCommandUpdatesExistingUser() {
        // Arrange
        var staleInput = UserInputFixture.valid().toBuilder()
                .name("Old").lastName("Name").phoneNumber("100000000").dni("99999999")
                .build();
        var existing = new User(IamCommandFixtures.createUserCommandFrom(staleInput));
        ReflectionTestUtils.setId(existing, 7L);
        var command = IamCommandFixtures.updateUserCommandFrom(7L, UserInputFixture.valid());
        when(userRepository.existsById(7L)).thenReturn(true);
        when(userRepository.findById(7L)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(existing);

        // Act
        Optional<User> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(CommonCommandFixtures.VALID_NAME);
        assertThat(result.get().getLastName()).isEqualTo(CommonCommandFixtures.VALID_LAST_NAME);
        assertThat(result.get().getPhoneNumber()).isEqualTo(CommonCommandFixtures.VALID_PHONE_NUMBER);
        assertThat(result.get().getDni()).isEqualTo(CommonCommandFixtures.VALID_DNI);
        verify(userRepository).existsById(7L);
        verify(userRepository).findById(7L);
        verify(userRepository).save(existing);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("handle(UpdateUserCommand) -> throws RuntimeException when user id does not exist (AAA)")
    void handleUpdateUserCommandThrowsWhenMissing() {
        // Arrange
        var command = IamCommandFixtures.updateUserCommandFrom(404L, UserInputFixture.valid());
        when(userRepository.existsById(404L)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("404").contains("does not exist");
        verify(userRepository).existsById(404L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("handle(UpdateUserCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleUpdateUserCommandWrapsSaveFailure() {
        // Arrange
        var existing = new User(IamCommandFixtures.validCreateUserCommand());
        ReflectionTestUtils.setId(existing, 7L);
        var command = IamCommandFixtures.updateUserCommandFrom(7L, UserInputFixture.valid());
        when(userRepository.existsById(7L)).thenReturn(true);
        when(userRepository.findById(7L)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenThrow(new RuntimeException("constraint"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error updating user").contains("constraint");
        verify(userRepository).existsById(7L);
        verify(userRepository).findById(7L);
        verify(userRepository).save(existing);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("handle(DeleteUserCommand) -> deletes when user id exists (AAA)")
    void handleDeleteUserCommandDeletes() {
        // Arrange
        var command = new DeleteUserCommand(11L);
        when(userRepository.existsById(11L)).thenReturn(true);

        // Act
        service.handle(command);

        // Assert
        verify(userRepository).existsById(11L);
        verify(userRepository).deleteById(11L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("handle(DeleteUserCommand) -> throws RuntimeException when user id is absent (AAA)")
    void handleDeleteUserCommandThrowsWhenMissing() {
        // Arrange
        var command = new DeleteUserCommand(11L);
        when(userRepository.existsById(11L)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("11").contains("does not exist");
        verify(userRepository).existsById(11L);
        verify(userRepository, never()).deleteById(any(Long.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("handle(DeleteUserCommand) -> wraps deleteById failure in RuntimeException (AAA)")
    void handleDeleteUserCommandWrapsDeleteFailure() {
        // Arrange
        var command = new DeleteUserCommand(11L);
        when(userRepository.existsById(11L)).thenReturn(true);
        org.mockito.Mockito.doThrow(new RuntimeException("fk violation")).when(userRepository).deleteById(11L);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error deleting user").contains("fk violation");
        verify(userRepository).existsById(11L);
        verify(userRepository).deleteById(11L);
        verifyNoMoreInteractions(userRepository);
    }
}
