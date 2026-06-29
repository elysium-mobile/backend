package pe.edu.upc.soft.work.platform.worker.forum.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Forum;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteCategoryCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Category;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.CategoryRepository;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.ForumRepository;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.ThreadRepository;
import pe.edu.upc.soft.work.platform.worker.forum.test.fixtures.WorkerForumCommandFixtures;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryCommandServiceImplTest {

    private static final Long CATEGORY_ID = 41L;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ForumRepository forumRepository;

    @Mock
    private ThreadRepository threadRepository;

    @InjectMocks
    private CategoryCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateCategoryCommand) -> creates Category and returns generated id (AAA)")
    void handleCreateSuccess() {
        // Arrange
        var command = WorkerForumCommandFixtures.validCreateCategoryCommand();
        var forum = mock(Forum.class);
        when(forumRepository.existsById(command.forumId())).thenReturn(true);
        when(forumRepository.findById(command.forumId())).thenReturn(Optional.of(forum));

        when(categoryRepository.save(any(Category.class))).thenAnswer(inv -> {
            Category c = inv.getArgument(0);
            ReflectionTestUtils.setId(c, CATEGORY_ID);
            return c;
        });

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(CATEGORY_ID);
        verify(forumRepository, times(1)).existsById(command.forumId());
        verify(forumRepository, times(1)).findById(command.forumId());
        verify(categoryRepository, times(1)).save(any(Category.class));
        verify(forumRepository, times(1)).save(forum);
        verifyNoMoreInteractions(categoryRepository, forumRepository);
    }

    @Test
    @DisplayName("handle(CreateCategoryCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleCreateSaveFailure() {
        var command = WorkerForumCommandFixtures.validCreateCategoryCommand();
        var forum = mock(Forum.class);

        when(forumRepository.existsById(command.forumId())).thenReturn(true);
        when(forumRepository.findById(command.forumId())).thenReturn(Optional.of(forum));
        when(categoryRepository.save(any(Category.class))).thenThrow(new RuntimeException("db"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error creating Category").contains("db");
        verify(forumRepository).existsById(command.forumId());
        verify(forumRepository).findById(command.forumId());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("handle(UpdateCategoryCommand) -> returns Optional with updated Category when present (AAA)")
    void handleUpdateSuccess() {
        // Arrange
        var existing = new Category(WorkerForumCommandFixtures.validCreateCategoryCommand());
        ReflectionTestUtils.setId(existing, CATEGORY_ID);
        var command = WorkerForumCommandFixtures.updateCategoryCommand(CATEGORY_ID);
        when(forumRepository.existsById(command.forumId())).thenReturn(true);
        when(categoryRepository.existsById(CATEGORY_ID)).thenReturn(true);
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(existing)).thenReturn(existing);

        // Act
        Optional<Category> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo(WorkerForumCommandFixtures.VALID_CATEGORY_TITLE);
        assertThat(result.get().getDescription()).isEqualTo(WorkerForumCommandFixtures.VALID_CATEGORY_DESCRIPTION);
        verify(forumRepository, times(1)).existsById(command.forumId());
        verify(categoryRepository, times(1)).existsById(CATEGORY_ID);
        verify(categoryRepository, times(1)).findById(CATEGORY_ID);
        verify(categoryRepository, times(1)).save(existing);
        verifyNoMoreInteractions(categoryRepository, forumRepository);
    }

    @Test
    @DisplayName("handle(UpdateCategoryCommand) -> throws RuntimeException when id does not exist (AAA)")
    void handleUpdateMissing() {
        // Arrange
        var command = WorkerForumCommandFixtures.updateCategoryCommand(CATEGORY_ID);
        when(forumRepository.existsById(command.forumId())).thenReturn(true);
        when(categoryRepository.existsById(CATEGORY_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(CATEGORY_ID)).contains("does not exist");
        verify(forumRepository, times(1)).existsById(command.forumId());
        verify(categoryRepository, times(1)).existsById(CATEGORY_ID);
        verifyNoMoreInteractions(categoryRepository, forumRepository);
    }

    @Test
    @DisplayName("handle(UpdateCategoryCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleUpdateSaveFailure() {

        // Arrange
        var existing = new Category(WorkerForumCommandFixtures.validCreateCategoryCommand());
        ReflectionTestUtils.setId(existing, CATEGORY_ID);
        var command = WorkerForumCommandFixtures.updateCategoryCommand(CATEGORY_ID);
        when(forumRepository.existsById(command.forumId())).thenReturn(true);
        when(categoryRepository.existsById(CATEGORY_ID)).thenReturn(true);
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(existing)).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error updating Category").contains("boom");
        verify(forumRepository, times(1)).existsById(command.forumId());
        verify(categoryRepository, times(1)).existsById(CATEGORY_ID);
        verify(categoryRepository, times(1)).findById(CATEGORY_ID);
        verify(categoryRepository, times(1)).save(existing);
        verifyNoMoreInteractions(categoryRepository, forumRepository);
    }

    @Test
    @DisplayName("handle(DeleteCategoryCommand) -> deletes when present (AAA)")
    void handleDeleteSuccess() {
        // Arrange
        var command = new DeleteCategoryCommand(CATEGORY_ID);
        var category = new Category();
        ReflectionTestUtils.setId(category, CATEGORY_ID);
        category.setForumId(1L);

        var forum = mock(Forum.class);

        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        when(forumRepository.findById(1L)).thenReturn(Optional.of(forum));

        // Act
        service.handle(command);

        // Assert
        verify(forum).removeCategory(CATEGORY_ID);
        verify(forumRepository).save(forum);
        verifyNoMoreInteractions(categoryRepository, forumRepository);
    }

    @Test
    @DisplayName("handle(DeleteCategoryCommand) -> throws RuntimeException when id is absent (AAA)")
    void handleDeleteMissing() {
        // Arrange
        var command = new DeleteCategoryCommand(CATEGORY_ID);
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.empty());

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(CATEGORY_ID)).contains("does not exist");
        verify(categoryRepository, times(1)).findById(CATEGORY_ID);
        verifyNoMoreInteractions(categoryRepository);
        verifyNoInteractions(forumRepository);
    }

    @Test
    @DisplayName("handle(DeleteCategoryCommand) -> wraps deleteById failure in RuntimeException (AAA)")
    void handleDeleteDeleteFailure() {
        // Arrange
        var command = new DeleteCategoryCommand(CATEGORY_ID);
        var category = new Category();
        ReflectionTestUtils.setId(category, CATEGORY_ID);
        category.setForumId(1L);

        var forum = mock(Forum.class);
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        when(forumRepository.findById(1L)).thenReturn(Optional.of(forum));
        doThrow(new RuntimeException("fk")).when(forumRepository).save(any(Forum.class));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error deleting Category").contains("fk");
        verify(categoryRepository).findById(CATEGORY_ID);
        verify(forumRepository).findById(1L);
        verify(forum).removeCategory(CATEGORY_ID);
        verify(forumRepository).save(any(Forum.class));
        verifyNoMoreInteractions(categoryRepository, forumRepository);
    }
}
