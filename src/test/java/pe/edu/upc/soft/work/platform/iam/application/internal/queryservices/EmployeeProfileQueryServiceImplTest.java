package pe.edu.upc.soft.work.platform.iam.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.iam.domain.model.entities.EmployeeProfile;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetAllEmployeeProfileQuery;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetEmployeeProfileByIdQuery;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.EmployeeProfileRepository;
import pe.edu.upc.soft.work.platform.iam.test.fixtures.IamCommandFixtures;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeProfileQueryServiceImplTest {

    @Mock
    private EmployeeProfileRepository employeeProfileRepository;

    @InjectMocks
    private EmployeeProfileQueryServiceImpl service;

    private static EmployeeProfile sample() {
        return new EmployeeProfile(IamCommandFixtures.validCreateEmployeeProfileCommand(10L, 3L));
    }

    @Test
    @DisplayName("handle(GetAllEmployeeProfileQuery) -> returns list from repository (AAA)")
    void handleAllReturnsList() {
        // Arrange
        List<EmployeeProfile> profiles = List.of(sample());
        when(employeeProfileRepository.findAll()).thenReturn(profiles);

        // Act
        List<EmployeeProfile> result = service.handle(new GetAllEmployeeProfileQuery());

        // Assert
        assertThat(result).containsExactlyElementsOf(profiles);
        verify(employeeProfileRepository).findAll();
        verifyNoMoreInteractions(employeeProfileRepository);
    }

    @Test
    @DisplayName("handle(GetAllEmployeeProfileQuery) -> returns empty list when none exist (AAA)")
    void handleAllReturnsEmpty() {
        // Arrange
        when(employeeProfileRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<EmployeeProfile> result = service.handle(new GetAllEmployeeProfileQuery());

        // Assert
        assertThat(result).isEmpty();
        verify(employeeProfileRepository).findAll();
        verifyNoMoreInteractions(employeeProfileRepository);
    }

    @Test
    @DisplayName("handle(GetEmployeeProfileByIdQuery) -> returns Optional with profile when found (AAA)")
    void handleByIdReturnsPresent() {
        // Arrange
        var profile = sample();
        when(employeeProfileRepository.findById(9L)).thenReturn(Optional.of(profile));

        // Act
        Optional<EmployeeProfile> result = service.handle(new GetEmployeeProfileByIdQuery(9L));

        // Assert
        assertThat(result).isPresent().containsSame(profile);
        verify(employeeProfileRepository).findById(9L);
        verifyNoMoreInteractions(employeeProfileRepository);
    }

    @Test
    @DisplayName("handle(GetEmployeeProfileByIdQuery) -> returns Optional.empty when no profile found (AAA)")
    void handleByIdReturnsEmpty() {
        // Arrange
        when(employeeProfileRepository.findById(9L)).thenReturn(Optional.empty());

        // Act
        Optional<EmployeeProfile> result = service.handle(new GetEmployeeProfileByIdQuery(9L));

        // Assert
        assertThat(result).isEmpty();
        verify(employeeProfileRepository).findById(9L);
        verifyNoMoreInteractions(employeeProfileRepository);
    }
}
