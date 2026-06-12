package pe.edu.upc.soft.work.platform.worker.forum.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Category;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAllCategoryQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetCategoryByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.CategoryRepository;
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
class CategoryQueryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryQueryServiceImpl service;

    private static Category sample() {
        return new Category(WorkerForumCommandFixtures.validCreateCategoryCommand());
    }

    @Test
    @DisplayName("handle(GetAllCategoryQuery) -> returns list from repository (AAA)")
    void handleAllReturnsList() {
        // Arrange
        List<Category> categories = List.of(sample());
        when(categoryRepository.findAll()).thenReturn(categories);

        // Act
        List<Category> result = service.handle(new GetAllCategoryQuery());

        // Assert
        assertThat(result).containsExactlyElementsOf(categories);
        verify(categoryRepository, times(1)).findAll();
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("handle(GetAllCategoryQuery) -> returns empty list when none exist (AAA)")
    void handleAllReturnsEmpty() {
        // Arrange
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Category> result = service.handle(new GetAllCategoryQuery());

        // Assert
        assertThat(result).isEmpty();
        verify(categoryRepository, times(1)).findAll();
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("handle(GetCategoryByIdQuery) -> returns Optional with Category when found (AAA)")
    void handleByIdReturnsPresent() {
        // Arrange
        var category = sample();
        when(categoryRepository.findById(41L)).thenReturn(Optional.of(category));

        // Act
        Optional<Category> result = service.handle(new GetCategoryByIdQuery(41L));

        // Assert
        assertThat(result).isPresent().containsSame(category);
        verify(categoryRepository, times(1)).findById(41L);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("handle(GetCategoryByIdQuery) -> returns Optional.empty when no Category found (AAA)")
    void handleByIdReturnsEmpty() {
        // Arrange
        when(categoryRepository.findById(41L)).thenReturn(Optional.empty());

        // Act
        Optional<Category> result = service.handle(new GetCategoryByIdQuery(41L));

        // Assert
        assertThat(result).isEmpty();
        verify(categoryRepository, times(1)).findById(41L);
        verifyNoMoreInteractions(categoryRepository);
    }
}
