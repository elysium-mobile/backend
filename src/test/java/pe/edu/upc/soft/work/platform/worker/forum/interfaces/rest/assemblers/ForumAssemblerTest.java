package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.assemblers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Forum;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateForumCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateForumCommand;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.CreateForumRequest;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.ForumResponse;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.UpdateForumRequest;
import pe.edu.upc.soft.work.platform.worker.forum.test.fixtures.WorkerForumCommandFixtures;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class ForumAssemblerTest {

    @Test
    @DisplayName("toCommandFromRequest(CreateForumRequest) -> wraps companyId in VO and maps fields (AAA)")
    void toCommandFromCreateRequestMapsAllFields() {
        // Arrange
        var request = new CreateForumRequest(
                WorkerForumCommandFixtures.VALID_FORUM_TITLE,
                WorkerForumCommandFixtures.VALID_FORUM_DESCRIPTION,
                WorkerForumCommandFixtures.VALID_COMPANY_ID);

        // Act
        CreateForumCommand command = ForumAssembler.toCommandFromRequest(request);

        // Assert
        assertThat(command.title()).isEqualTo(WorkerForumCommandFixtures.VALID_FORUM_TITLE);
        assertThat(command.description()).isEqualTo(WorkerForumCommandFixtures.VALID_FORUM_DESCRIPTION);
        assertThat(command.companyId().companyId()).isEqualTo(WorkerForumCommandFixtures.VALID_COMPANY_ID);
    }

    @Test
    @DisplayName("toCommandFromRequest(Long, UpdateForumRequest) -> maps id, wraps companyId VO and fields (AAA)")
    void toCommandFromUpdateRequestMapsAllFields() {
        // Arrange
        var request = new UpdateForumRequest(
                WorkerForumCommandFixtures.VALID_FORUM_TITLE,
                WorkerForumCommandFixtures.VALID_FORUM_DESCRIPTION,
                WorkerForumCommandFixtures.VALID_COMPANY_ID);

        // Act
        UpdateForumCommand command = ForumAssembler.toCommandFromRequest(51L, request);

        // Assert
        assertThat(command.forumId()).isEqualTo(51L);
        assertThat(command.title()).isEqualTo(WorkerForumCommandFixtures.VALID_FORUM_TITLE);
        assertThat(command.description()).isEqualTo(WorkerForumCommandFixtures.VALID_FORUM_DESCRIPTION);
        assertThat(command.companyId().companyId()).isEqualTo(WorkerForumCommandFixtures.VALID_COMPANY_ID);
    }

    @Test
    @DisplayName("toResponseFromEntity(Forum) -> unwraps companyId VO and maps every field (AAA)")
    void toResponseFromEntityMapsAllFields() {
        // Arrange
        var entity = new Forum(WorkerForumCommandFixtures.validCreateForumCommand());
        ReflectionTestUtils.setId(entity, 51L);
        ReflectionTestUtils.setField(entity, "categories", new ArrayList<>());

        // Act
        ForumResponse response = ForumAssembler.toResponseFromEntity(entity);

        // Assert
        assertThat(response.forumId()).isEqualTo(51L);
        assertThat(response.title()).isEqualTo(WorkerForumCommandFixtures.VALID_FORUM_TITLE);
        assertThat(response.description()).isEqualTo(WorkerForumCommandFixtures.VALID_FORUM_DESCRIPTION);
        assertThat(response.companyId()).isEqualTo(WorkerForumCommandFixtures.VALID_COMPANY_ID);
    }
}
