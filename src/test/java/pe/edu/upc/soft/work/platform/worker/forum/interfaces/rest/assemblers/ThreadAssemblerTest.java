package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.assemblers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Thread;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateThreadCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateThreadCommand;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.CreateThreadRequest;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.ThreadResponse;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.UpdateThreadRequest;
import pe.edu.upc.soft.work.platform.worker.forum.test.fixtures.WorkerForumCommandFixtures;

import static org.assertj.core.api.Assertions.assertThat;

class ThreadAssemblerTest {

    @Test
    @DisplayName("toCommandFromRequest(CreateThreadRequest) -> wraps areaCompanyId in VO and maps fields (AAA)")
    void toCommandFromCreateRequestMapsAllFields() {
        // Arrange
        var request = new CreateThreadRequest(
                WorkerForumCommandFixtures.VALID_THREAD_TITLE,
                WorkerForumCommandFixtures.VALID_AREA_COMPANY_ID,
                WorkerForumCommandFixtures.VALID_LAST_MESSAGE);

        // Act
        CreateThreadCommand command = ThreadAssembler.toCommandFromRequest(request);

        // Assert
        assertThat(command.title()).isEqualTo(WorkerForumCommandFixtures.VALID_THREAD_TITLE);
        assertThat(command.areaCompanyId().areaCompanyId())
                .isEqualTo(WorkerForumCommandFixtures.VALID_AREA_COMPANY_ID);
        assertThat(command.lastMessage()).isEqualTo(WorkerForumCommandFixtures.VALID_LAST_MESSAGE);
    }

    @Test
    @DisplayName("toCommandFromRequest(Long, UpdateThreadRequest) -> maps id, wraps areaCompanyId VO and fields (AAA)")
    void toCommandFromUpdateRequestMapsAllFields() {
        // Arrange
        var request = new UpdateThreadRequest(
                WorkerForumCommandFixtures.VALID_THREAD_TITLE,
                WorkerForumCommandFixtures.VALID_AREA_COMPANY_ID,
                WorkerForumCommandFixtures.VALID_LAST_MESSAGE);

        // Act
        UpdateThreadCommand command = ThreadAssembler.toCommandFromRequest(71L, request);

        // Assert
        assertThat(command.threadId()).isEqualTo(71L);
        assertThat(command.title()).isEqualTo(WorkerForumCommandFixtures.VALID_THREAD_TITLE);
        assertThat(command.areaCompanyId().areaCompanyId())
                .isEqualTo(WorkerForumCommandFixtures.VALID_AREA_COMPANY_ID);
        assertThat(command.lastMessage()).isEqualTo(WorkerForumCommandFixtures.VALID_LAST_MESSAGE);
    }

    @Test
    @DisplayName("toResponseFromEntity(Thread) -> unwraps areaCompanyId VO and maps every field (AAA)")
    void toResponseFromEntityMapsAllFields() {
        // Arrange
        var entity = new Thread(WorkerForumCommandFixtures.validCreateThreadCommand());
        ReflectionTestUtils.setId(entity, 71L);

        // Act
        ThreadResponse response = ThreadAssembler.toResponseFromEntity(entity);

        // Assert
        assertThat(response.threadId()).isEqualTo(71L);
        assertThat(response.title()).isEqualTo(WorkerForumCommandFixtures.VALID_THREAD_TITLE);
        assertThat(response.areaCompanyId()).isEqualTo(WorkerForumCommandFixtures.VALID_AREA_COMPANY_ID);
        assertThat(response.lastMessage()).isEqualTo(WorkerForumCommandFixtures.VALID_LAST_MESSAGE);
    }
}
