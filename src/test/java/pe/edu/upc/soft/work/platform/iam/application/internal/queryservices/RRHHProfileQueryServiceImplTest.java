package pe.edu.upc.soft.work.platform.iam.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.iam.domain.model.entities.RRHHProfile;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetAllRRHHProfileQuery;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetRRHHProfileByIdQuery;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.RRHHProfileRepository;
import pe.edu.upc.soft.work.platform.iam.test.fixtures.IamCommandFixtures;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RRHHProfileQueryServiceImplTest {

    @Mock
    private RRHHProfileRepository rrhhProfileRepository;

    @InjectMocks
    private RRHHProfileQueryServiceImpl service;

    private static RRHHProfile sample() {
        return new RRHHProfile(IamCommandFixtures.validCreateRRHHProfileCommand(10L));
    }

    @Test
    @DisplayName("handle(GetAllRRHHProfileQuery) -> returns list from repository (AAA)")
    void handleAllReturnsList() {
        // Arrange
        List<RRHHProfile> profiles = List.of(sample());
        when(rrhhProfileRepository.findAll()).thenReturn(profiles);

        // Act
        List<RRHHProfile> result = service.handle(new GetAllRRHHProfileQuery());

        // Assert
        assertThat(result).containsExactlyElementsOf(profiles);
        verify(rrhhProfileRepository).findAll();
        verifyNoMoreInteractions(rrhhProfileRepository);
    }

    @Test
    @DisplayName("handle(GetAllRRHHProfileQuery) -> returns empty list when none exist (AAA)")
    void handleAllReturnsEmpty() {
        // Arrange
        when(rrhhProfileRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<RRHHProfile> result = service.handle(new GetAllRRHHProfileQuery());

        // Assert
        assertThat(result).isEmpty();
        verify(rrhhProfileRepository).findAll();
        verifyNoMoreInteractions(rrhhProfileRepository);
    }

    @Test
    @DisplayName("handle(GetRRHHProfileByIdQuery) -> returns Optional with profile when found (AAA)")
    void handleByIdReturnsPresent() {
        // Arrange
        var profile = sample();
        when(rrhhProfileRepository.findById(15L)).thenReturn(Optional.of(profile));

        // Act
        Optional<RRHHProfile> result = service.handle(new GetRRHHProfileByIdQuery(15L));

        // Assert
        assertThat(result).isPresent().containsSame(profile);
        verify(rrhhProfileRepository).findById(15L);
        verifyNoMoreInteractions(rrhhProfileRepository);
    }

    @Test
    @DisplayName("handle(GetRRHHProfileByIdQuery) -> returns Optional.empty when no profile found (AAA)")
    void handleByIdReturnsEmpty() {
        // Arrange
        when(rrhhProfileRepository.findById(15L)).thenReturn(Optional.empty());

        // Act
        Optional<RRHHProfile> result = service.handle(new GetRRHHProfileByIdQuery(15L));

        // Assert
        assertThat(result).isEmpty();
        verify(rrhhProfileRepository).findById(15L);
        verifyNoMoreInteractions(rrhhProfileRepository);
    }
}
