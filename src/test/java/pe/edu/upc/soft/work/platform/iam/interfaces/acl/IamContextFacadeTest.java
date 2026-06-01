package pe.edu.upc.soft.work.platform.iam.interfaces.acl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;
import pe.edu.upc.soft.work.platform.iam.domain.model.entities.EmployeeProfile;
import pe.edu.upc.soft.work.platform.iam.domain.model.entities.RRHHProfile;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetEmployeeProfileByIdQuery;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetRRHHProfileByIdQuery;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetUserAccountByIdQuery;
import pe.edu.upc.soft.work.platform.iam.domain.services.EmployeeProfileQueryService;
import pe.edu.upc.soft.work.platform.iam.domain.services.RRHHProfileQueryService;
import pe.edu.upc.soft.work.platform.iam.domain.services.UserAccountQueryService;
import pe.edu.upc.soft.work.platform.iam.test.fixtures.IamCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.fixtures.UserInputFixture;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IamContextFacadeTest {

    @Mock
    private UserAccountQueryService userAccountQueryService;
    @Mock
    private EmployeeProfileQueryService employeeProfileQueryService;
    @Mock
    private RRHHProfileQueryService rrhhProfileQueryService;

    @InjectMocks
    private IamContextFacade facade;

    @Test
    @DisplayName("existsUserAccountById(Long) -> returns true when query service returns Optional with value (AAA)")
    void existsUserAccountByIdPresent() {
        // Arrange
        var account = new UserAccount(
                IamCommandFixtures.createUserAccountCommandFrom(1L, UserInputFixture.valid()));
        when(userAccountQueryService.handle(any(GetUserAccountByIdQuery.class))).thenReturn(Optional.of(account));

        // Act
        boolean result = facade.existsUserAccountById(5L);

        // Assert
        assertThat(result).isTrue();
        verify(userAccountQueryService).handle(any(GetUserAccountByIdQuery.class));
        verifyNoMoreInteractions(userAccountQueryService);
        verifyNoInteractions(employeeProfileQueryService, rrhhProfileQueryService);
    }

    @Test
    @DisplayName("existsUserAccountById(Long) -> returns false when query service returns Optional.empty (AAA)")
    void existsUserAccountByIdAbsent() {
        // Arrange
        when(userAccountQueryService.handle(any(GetUserAccountByIdQuery.class))).thenReturn(Optional.empty());

        // Act
        boolean result = facade.existsUserAccountById(5L);

        // Assert
        assertThat(result).isFalse();
        verify(userAccountQueryService).handle(any(GetUserAccountByIdQuery.class));
        verifyNoMoreInteractions(userAccountQueryService);
        verifyNoInteractions(employeeProfileQueryService, rrhhProfileQueryService);
    }

    @Test
    @DisplayName("existsEmployeeProfileById(Long) -> returns true when query service returns Optional with value (AAA)")
    void existsEmployeeProfileByIdPresent() {
        // Arrange
        var profile = new EmployeeProfile(IamCommandFixtures.validCreateEmployeeProfileCommand(1L, 1L));
        when(employeeProfileQueryService.handle(any(GetEmployeeProfileByIdQuery.class)))
                .thenReturn(Optional.of(profile));

        // Act
        boolean result = facade.existsEmployeeProfileById(7L);

        // Assert
        assertThat(result).isTrue();
        verify(employeeProfileQueryService).handle(any(GetEmployeeProfileByIdQuery.class));
        verifyNoMoreInteractions(employeeProfileQueryService);
        verifyNoInteractions(userAccountQueryService, rrhhProfileQueryService);
    }

    @Test
    @DisplayName("existsEmployeeProfileById(Long) -> returns false when query service returns Optional.empty (AAA)")
    void existsEmployeeProfileByIdAbsent() {
        // Arrange
        when(employeeProfileQueryService.handle(any(GetEmployeeProfileByIdQuery.class))).thenReturn(Optional.empty());

        // Act
        boolean result = facade.existsEmployeeProfileById(7L);

        // Assert
        assertThat(result).isFalse();
        verify(employeeProfileQueryService).handle(any(GetEmployeeProfileByIdQuery.class));
        verifyNoMoreInteractions(employeeProfileQueryService);
        verifyNoInteractions(userAccountQueryService, rrhhProfileQueryService);
    }

    @Test
    @DisplayName("existsRRHHProfileById(Long) -> returns true when query service returns Optional with value (AAA)")
    void existsRRHHProfileByIdPresent() {
        // Arrange
        var rrhh = new RRHHProfile(IamCommandFixtures.validCreateRRHHProfileCommand(1L));
        when(rrhhProfileQueryService.handle(any(GetRRHHProfileByIdQuery.class))).thenReturn(Optional.of(rrhh));

        // Act
        boolean result = facade.existsRRHHProfileById(9L);

        // Assert
        assertThat(result).isTrue();
        verify(rrhhProfileQueryService).handle(any(GetRRHHProfileByIdQuery.class));
        verifyNoMoreInteractions(rrhhProfileQueryService);
        verifyNoInteractions(userAccountQueryService, employeeProfileQueryService);
    }

    @Test
    @DisplayName("existsRRHHProfileById(Long) -> returns false when query service returns Optional.empty (AAA)")
    void existsRRHHProfileByIdAbsent() {
        // Arrange
        when(rrhhProfileQueryService.handle(any(GetRRHHProfileByIdQuery.class))).thenReturn(Optional.empty());

        // Act
        boolean result = facade.existsRRHHProfileById(9L);

        // Assert
        assertThat(result).isFalse();
        verify(rrhhProfileQueryService).handle(any(GetRRHHProfileByIdQuery.class));
        verifyNoMoreInteractions(rrhhProfileQueryService);
        verifyNoInteractions(userAccountQueryService, employeeProfileQueryService);
    }
}
