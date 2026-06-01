package pe.edu.upc.soft.work.platform.worker.forum.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Forum;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAllForumQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetForumByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.ForumRepository;
import pe.edu.upc.soft.work.platform.worker.forum.test.fixtures.WorkerForumCommandFixtures;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ForumQueryServiceImplTest {

    @Mock
    private ForumRepository forumRepository;

    @InjectMocks
    private ForumQueryServiceImpl service;

    private static Forum sample() {
        return new Forum(WorkerForumCommandFixtures.validCreateForumCommand());
    }

    @Test
    @DisplayName("handle(GetAllForumQuery) -> returns list from repository (AAA)")
    void handleAllReturnsList() {
        // Arrange
        List<Forum> forums = List.of(sample());
        when(forumRepository.findAll()).thenReturn(forums);

        // Act
        List<Forum> result = service.handle(new GetAllForumQuery());

        // Assert
        assertThat(result).containsExactlyElementsOf(forums);
        verify(forumRepository, times(1)).findAll();
        verifyNoMoreInteractions(forumRepository);
    }

    @Test
    @DisplayName("handle(GetAllForumQuery) -> returns empty list when none exist (AAA)")
    void handleAllReturnsEmpty() {
        // Arrange
        when(forumRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Forum> result = service.handle(new GetAllForumQuery());

        // Assert
        assertThat(result).isEmpty();
        verify(forumRepository, times(1)).findAll();
        verifyNoMoreInteractions(forumRepository);
    }

    @Test
    @DisplayName("handle(GetForumByIdQuery) -> returns Optional with Forum when found (AAA)")
    void handleByIdReturnsPresent() {
        // Arrange
        var forum = sample();
        when(forumRepository.findById(51L)).thenReturn(Optional.of(forum));

        // Act
        Optional<Forum> result = service.handle(new GetForumByIdQuery(51L));

        // Assert
        assertThat(result).isPresent().containsSame(forum);
        verify(forumRepository, times(1)).findById(51L);
        verifyNoMoreInteractions(forumRepository);
    }

    @Test
    @DisplayName("handle(GetForumByIdQuery) -> returns Optional.empty when no Forum found (AAA)")
    void handleByIdReturnsEmpty() {
        // Arrange
        when(forumRepository.findById(51L)).thenReturn(Optional.empty());

        // Act
        Optional<Forum> result = service.handle(new GetForumByIdQuery(51L));

        // Assert
        assertThat(result).isEmpty();
        verify(forumRepository, times(1)).findById(51L);
        verifyNoMoreInteractions(forumRepository);
    }
}
