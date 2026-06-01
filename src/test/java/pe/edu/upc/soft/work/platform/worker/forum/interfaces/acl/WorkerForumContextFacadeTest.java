package pe.edu.upc.soft.work.platform.worker.forum.interfaces.acl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Forum;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateForumCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetForumByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.ForumCommandService;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.ForumQueryService;
import pe.edu.upc.soft.work.platform.worker.forum.test.fixtures.WorkerForumCommandFixtures;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkerForumContextFacadeTest {

    @Mock
    private ForumQueryService forumQueryService;
    @Mock
    private ForumCommandService forumCommandService;

    @InjectMocks
    private WorkerForumContextFacade facade;

    @Test
    @DisplayName("existsForumById(Long) -> returns true when query service returns Optional with value (AAA)")
    void existsForumByIdPresent() {
        // Arrange
        var forum = new Forum(WorkerForumCommandFixtures.validCreateForumCommand());
        when(forumQueryService.handle(any(GetForumByIdQuery.class))).thenReturn(Optional.of(forum));

        // Act
        boolean result = facade.existsForumById(51L);

        // Assert
        assertThat(result).isTrue();
        verify(forumQueryService, times(1)).handle(any(GetForumByIdQuery.class));
        verifyNoMoreInteractions(forumQueryService);
        verifyNoInteractions(forumCommandService);
    }

    @Test
    @DisplayName("existsForumById(Long) -> returns false when query service returns Optional.empty (AAA)")
    void existsForumByIdAbsent() {
        // Arrange
        when(forumQueryService.handle(any(GetForumByIdQuery.class))).thenReturn(Optional.empty());

        // Act
        boolean result = facade.existsForumById(51L);

        // Assert
        assertThat(result).isFalse();
        verify(forumQueryService, times(1)).handle(any(GetForumByIdQuery.class));
        verifyNoMoreInteractions(forumQueryService);
        verifyNoInteractions(forumCommandService);
    }

    @Test
    @DisplayName("createForum(...) -> returns id from command service when not null (AAA)")
    void createForumReturnsId() {
        // Arrange
        when(forumCommandService.handle(any(CreateForumCommand.class))).thenReturn(99L);

        // Act
        Long result = facade.createForum(
                WorkerForumCommandFixtures.VALID_FORUM_TITLE,
                WorkerForumCommandFixtures.VALID_FORUM_DESCRIPTION,
                WorkerForumCommandFixtures.VALID_COMPANY_ID);

        // Assert
        assertThat(result).isEqualTo(99L);
        verify(forumCommandService, times(1)).handle(any(CreateForumCommand.class));
        verifyNoMoreInteractions(forumCommandService);
        verifyNoInteractions(forumQueryService);
    }

    @Test
    @DisplayName("createForum(...) -> returns 0L when command service returns null (AAA)")
    void createForumReturnsZeroOnNull() {
        // Arrange
        when(forumCommandService.handle(any(CreateForumCommand.class))).thenReturn(null);

        // Act
        Long result = facade.createForum(
                WorkerForumCommandFixtures.VALID_FORUM_TITLE,
                WorkerForumCommandFixtures.VALID_FORUM_DESCRIPTION,
                WorkerForumCommandFixtures.VALID_COMPANY_ID);

        // Assert
        assertThat(result).isEqualTo(0L);
        verify(forumCommandService, times(1)).handle(any(CreateForumCommand.class));
        verifyNoMoreInteractions(forumCommandService);
        verifyNoInteractions(forumQueryService);
    }
}
