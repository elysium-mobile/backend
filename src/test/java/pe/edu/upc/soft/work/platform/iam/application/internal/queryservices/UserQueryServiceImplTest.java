package pe.edu.upc.soft.work.platform.iam.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.User;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetAllUsersQuery;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetUserByIdQuery;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import pe.edu.upc.soft.work.platform.iam.test.fixtures.IamCommandFixtures;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserQueryServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserQueryServiceImpl service;

    private static User sampleUser() {
        return new User(IamCommandFixtures.validCreateUserCommand());
    }

    @Test
    @DisplayName("handle(GetAllUsersQuery) -> returns the list provided by the repository (AAA)")
    void handleGetAllUsersReturnsList() {
        // Arrange
        List<User> repoUsers = List.of(sampleUser(), sampleUser());
        when(userRepository.findAll()).thenReturn(repoUsers);

        // Act
        List<User> result = service.handle(new GetAllUsersQuery());

        // Assert
        assertThat(result).hasSize(2).containsExactlyElementsOf(repoUsers);
        verify(userRepository).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("handle(GetAllUsersQuery) -> returns empty list when repository has no users (AAA)")
    void handleGetAllUsersReturnsEmpty() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<User> result = service.handle(new GetAllUsersQuery());

        // Assert
        assertThat(result).isEmpty();
        verify(userRepository).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("handle(GetUserByIdQuery) -> returns Optional with user when found (AAA)")
    void handleGetUserByIdReturnsPresent() {
        // Arrange
        var user = sampleUser();
        when(userRepository.findById(5L)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = service.handle(new GetUserByIdQuery(5L));

        // Assert
        assertThat(result).isPresent().containsSame(user);
        verify(userRepository).findById(5L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("handle(GetUserByIdQuery) -> returns Optional.empty when no user found (AAA)")
    void handleGetUserByIdReturnsEmpty() {
        // Arrange
        when(userRepository.findById(5L)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = service.handle(new GetUserByIdQuery(5L));

        // Assert
        assertThat(result).isEmpty();
        verify(userRepository).findById(5L);
        verifyNoMoreInteractions(userRepository);
    }
}
