package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.assemblers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateCategoryCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateCategoryCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Category;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.CategoryResponse;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.CreateCategoryRequest;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.UpdateCategoryRequest;
import pe.edu.upc.soft.work.platform.worker.forum.test.fixtures.WorkerForumCommandFixtures;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryAssemblerTest {

    @Test
    @DisplayName("toCommandFromRequest(CreateCategoryRequest) -> maps title and description (AAA)")
    void toCommandFromCreateRequestMapsAllFields() {
        // Arrange
        var request = new CreateCategoryRequest(
                WorkerForumCommandFixtures.VALID_CATEGORY_TITLE,
                WorkerForumCommandFixtures.VALID_CATEGORY_DESCRIPTION,WorkerForumCommandFixtures.VALID_FORUM_ID);

        // Act
        CreateCategoryCommand command = CategoryAssembler.toCommandFromRequest(request);

        // Assert
        assertThat(command.title()).isEqualTo(WorkerForumCommandFixtures.VALID_CATEGORY_TITLE);
        assertThat(command.description()).isEqualTo(WorkerForumCommandFixtures.VALID_CATEGORY_DESCRIPTION);
        assertThat(command.forumId()).isEqualTo(WorkerForumCommandFixtures.VALID_FORUM_ID);
    }

    @Test
    @DisplayName("toCommandFromRequest(Long, UpdateCategoryRequest) -> maps id, title and description (AAA)")
    void toCommandFromUpdateRequestMapsAllFields() {
        // Arrange
        var request = new UpdateCategoryRequest(
                WorkerForumCommandFixtures.VALID_CATEGORY_TITLE,
                WorkerForumCommandFixtures.VALID_CATEGORY_DESCRIPTION,
            WorkerForumCommandFixtures.VALID_FORUM_ID);

        // Act
        UpdateCategoryCommand command = CategoryAssembler.toCommandFromRequest(41L, request);

        // Assert
        assertThat(command.categoryId()).isEqualTo(41L);
        assertThat(command.title()).isEqualTo(WorkerForumCommandFixtures.VALID_CATEGORY_TITLE);
        assertThat(command.description()).isEqualTo(WorkerForumCommandFixtures.VALID_CATEGORY_DESCRIPTION);
        assertThat(command.forumId()).isEqualTo(WorkerForumCommandFixtures.VALID_FORUM_ID);
    }

    @Test
    @DisplayName("toResponseFromEntity(Category) -> maps id, title and description to CategoryResponse (AAA)")
    void toResponseFromEntityMapsAllFields() {
        // Arrange
        var entity = new Category(WorkerForumCommandFixtures.validCreateCategoryCommand());
        ReflectionTestUtils.setId(entity, 41L);

        // Act
        CategoryResponse response = CategoryAssembler.toResponseFromEntity(entity);

        // Assert
        assertThat(response.categoryId()).isEqualTo(41L);
        assertThat(response.title()).isEqualTo(WorkerForumCommandFixtures.VALID_CATEGORY_TITLE);
        assertThat(response.description()).isEqualTo(WorkerForumCommandFixtures.VALID_CATEGORY_DESCRIPTION);
        assertThat(response.forumId()).isEqualTo(WorkerForumCommandFixtures.VALID_FORUM_ID);
    }
}
