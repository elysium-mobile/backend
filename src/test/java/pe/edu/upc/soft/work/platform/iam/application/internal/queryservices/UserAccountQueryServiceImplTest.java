package pe.edu.upc.soft.work.platform.iam.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetAllUserAccountQuery;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetUserAccountByIdQuery;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.UserAccountRepository;
import pe.edu.upc.soft.work.platform.iam.test.fixtures.IamCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.fixtures.UserInputFixture;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAccountQueryServiceImplTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @InjectMocks
    private UserAccountQueryServiceImpl service;

    private static UserAccount sample() {
        return new UserAccount(IamCommandFixtures.createUserAccountCommandFrom(1L, UserInputFixture.valid()));
    }

    @Test
    @DisplayName("handle(GetAllUserAccountQuery) -> returns list from repository (AAA)")
    void handleAllReturnsList() {
        // Arrange
        List<UserAccount> accounts = List.of(sample());
        when(userAccountRepository.findAll()).thenReturn(accounts);

        // Act
        List<UserAccount> result = service.handle(new GetAllUserAccountQuery());

        // Assert
        assertThat(result).containsExactlyElementsOf(accounts);
        verify(userAccountRepository).findAll();
        verifyNoMoreInteractions(userAccountRepository);
    }

    @Test
    @DisplayName("handle(GetAllUserAccountQuery) -> returns empty list when none exist (AAA)")
    void handleAllReturnsEmpty() {
        // Arrange
        when(userAccountRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<UserAccount> result = service.handle(new GetAllUserAccountQuery());

        // Assert
        assertThat(result).isEmpty();
        verify(userAccountRepository).findAll();
        verifyNoMoreInteractions(userAccountRepository);
    }

    @Test
    @DisplayName("handle(GetUserAccountByIdQuery) -> returns Optional with account when found (AAA)")
    void handleByIdReturnsPresent() {
        // Arrange
        var account = sample();
        when(userAccountRepository.findById(3L)).thenReturn(Optional.of(account));

        // Act
        Optional<UserAccount> result = service.handle(new GetUserAccountByIdQuery(3L));

        // Assert
        assertThat(result).isPresent().containsSame(account);
        verify(userAccountRepository).findById(3L);
        verifyNoMoreInteractions(userAccountRepository);
    }

    @Test
    @DisplayName("handle(GetUserAccountByIdQuery) -> returns Optional.empty when no account found (AAA)")
    void handleByIdReturnsEmpty() {
        // Arrange
        when(userAccountRepository.findById(3L)).thenReturn(Optional.empty());

        // Act
        Optional<UserAccount> result = service.handle(new GetUserAccountByIdQuery(3L));

        // Assert
        assertThat(result).isEmpty();
        verify(userAccountRepository).findById(3L);
        verifyNoMoreInteractions(userAccountRepository);
    }
}
