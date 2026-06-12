package pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.assemblers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.aggregates.CommentEmployee;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.CreateCommentEmployeeCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.UpdateCommentEmployeeCommand;
import pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources.CommentEmployeeResponse;
import pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources.CreateCommentEmployeeRequest;
import pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources.UpdateCommentEmployeeRequest;
import pe.edu.upc.soft.work.platform.profile.performance.test.fixtures.ProfilePerformanceCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class CommentEmployeeAssemblerTest {

    @Test
    @DisplayName("toCommandFromRequest(CreateCommentEmployeeRequest) -> wraps rrhhProfileId in VO and maps fields (AAA)")
    void toCommandFromCreateRequestMapsAllFields() {
        // Arrange
        var request = new CreateCommentEmployeeRequest(
                ProfilePerformanceCommandFixtures.VALID_COMMENT_TITLE,
                ProfilePerformanceCommandFixtures.VALID_COMMENT_CONTENT,
                ProfilePerformanceCommandFixtures.VALID_RRHH_PROFILE_ID,
            ProfilePerformanceCommandFixtures.VALID_PERFORMANCE_ID);

        // Act
        CreateCommentEmployeeCommand command = CommentEmployeeAssembler.toCommandFromRequest(request);

        // Assert
        assertThat(command.title()).isEqualTo(ProfilePerformanceCommandFixtures.VALID_COMMENT_TITLE);
        assertThat(command.content()).isEqualTo(ProfilePerformanceCommandFixtures.VALID_COMMENT_CONTENT);
        assertThat(command.rrhhProfileId().rrhhProfileId())
                .isEqualTo(ProfilePerformanceCommandFixtures.VALID_RRHH_PROFILE_ID);
        assertThat(command.performanceId()).isEqualTo(ProfilePerformanceCommandFixtures.VALID_PERFORMANCE_ID);
    }

    @Test
    @DisplayName("toCommandFromRequest(Long, UpdateCommentEmployeeRequest) -> maps id, fields and wraps rrhhProfileId VO (AAA)")
    void toCommandFromUpdateRequestMapsAllFields() {
        // Arrange
        var request = new UpdateCommentEmployeeRequest(
                ProfilePerformanceCommandFixtures.VALID_COMMENT_TITLE,
                ProfilePerformanceCommandFixtures.VALID_COMMENT_CONTENT,
                ProfilePerformanceCommandFixtures.VALID_RRHH_PROFILE_ID,
            ProfilePerformanceCommandFixtures.VALID_PERFORMANCE_ID);

        // Act
        UpdateCommentEmployeeCommand command = CommentEmployeeAssembler.toCommandFromRequest(13L, request);

        // Assert
        assertThat(command.commentEmployeeId()).isEqualTo(13L);
        assertThat(command.title()).isEqualTo(ProfilePerformanceCommandFixtures.VALID_COMMENT_TITLE);
        assertThat(command.content()).isEqualTo(ProfilePerformanceCommandFixtures.VALID_COMMENT_CONTENT);
        assertThat(command.rrhhProfileId().rrhhProfileId())
                .isEqualTo(ProfilePerformanceCommandFixtures.VALID_RRHH_PROFILE_ID);
        assertThat(command.performanceId()).isEqualTo(ProfilePerformanceCommandFixtures.VALID_PERFORMANCE_ID);
    }

    @Test
    @DisplayName("toResponseFromEntity(CommentEmployee) -> unwraps rrhhProfileId VO and maps every field (AAA)")
    void toResponseFromEntityMapsAllFields() {
        // Arrange
        var entity = new CommentEmployee(ProfilePerformanceCommandFixtures.validCreateCommentEmployeeCommand());
        ReflectionTestUtils.setId(entity, 13L);

        // Act
        CommentEmployeeResponse response = CommentEmployeeAssembler.toResponseFromEntity(entity);

        // Assert
        assertThat(response.commentEmployeeId()).isEqualTo(13L);
        assertThat(response.title()).isEqualTo(ProfilePerformanceCommandFixtures.VALID_COMMENT_TITLE);
        assertThat(response.content()).isEqualTo(ProfilePerformanceCommandFixtures.VALID_COMMENT_CONTENT);
        assertThat(response.rrhhProfileId()).isEqualTo(ProfilePerformanceCommandFixtures.VALID_RRHH_PROFILE_ID);
        assertThat(response.performanceId()).isEqualTo(ProfilePerformanceCommandFixtures.VALID_PERFORMANCE_ID);
    }
}
