package pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.assemblers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.aggregates.Performance;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.CreatePerformanceCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.UpdatePerformanceCommand;
import pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources.CreatePerformanceRequest;
import pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources.PerformanceResponse;
import pe.edu.upc.soft.work.platform.profile.performance.interfaces.rest.resources.UpdatePerformanceRequest;
import pe.edu.upc.soft.work.platform.profile.performance.test.fixtures.ProfilePerformanceCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class PerformanceAssemblerTest {

    @Test
    @DisplayName("toCommandFromRequest(CreatePerformanceRequest) -> wraps employeeProfileId in VO and maps fields (AAA)")
    void toCommandFromCreateRequestMapsAllFields() {
        // Arrange
        var request = new CreatePerformanceRequest(
                ProfilePerformanceCommandFixtures.VALID_EMPLOYEE_PROFILE_ID,
                ProfilePerformanceCommandFixtures.VALID_PERFORMANCE_DATE,
                ProfilePerformanceCommandFixtures.VALID_CLASSIFICATION);

        // Act
        CreatePerformanceCommand command = PerformanceAssembler.toCommandFromRequest(request);

        // Assert
        assertThat(command.employeeProfileId().employeeProfileId())
                .isEqualTo(ProfilePerformanceCommandFixtures.VALID_EMPLOYEE_PROFILE_ID);
        assertThat(command.dateTime()).isEqualTo(ProfilePerformanceCommandFixtures.VALID_PERFORMANCE_DATE);
        assertThat(command.classification()).isEqualTo(ProfilePerformanceCommandFixtures.VALID_CLASSIFICATION);
    }

    @Test
    @DisplayName("toCommandFromRequest(Long, UpdatePerformanceRequest) -> maps id, wraps employeeProfileId VO and fields (AAA)")
    void toCommandFromUpdateRequestMapsAllFields() {
        // Arrange
        var request = new UpdatePerformanceRequest(
                ProfilePerformanceCommandFixtures.VALID_EMPLOYEE_PROFILE_ID,
                ProfilePerformanceCommandFixtures.VALID_PERFORMANCE_DATE,
                ProfilePerformanceCommandFixtures.VALID_CLASSIFICATION);

        // Act
        UpdatePerformanceCommand command = PerformanceAssembler.toCommandFromRequest(23L, request);

        // Assert
        assertThat(command.performanceId()).isEqualTo(23L);
        assertThat(command.employeeProfileId().employeeProfileId())
                .isEqualTo(ProfilePerformanceCommandFixtures.VALID_EMPLOYEE_PROFILE_ID);
        assertThat(command.dateTime()).isEqualTo(ProfilePerformanceCommandFixtures.VALID_PERFORMANCE_DATE);
        assertThat(command.classification()).isEqualTo(ProfilePerformanceCommandFixtures.VALID_CLASSIFICATION);
    }

    @Test
    @DisplayName("toResponseFromEntity(Performance) -> unwraps employeeProfileId VO and maps every field (AAA)")
    void toResponseFromEntityMapsAllFields() {
        // Arrange
        var entity = new Performance(ProfilePerformanceCommandFixtures.validCreatePerformanceCommand());
        ReflectionTestUtils.setId(entity, 23L);
        ReflectionTestUtils.setField(entity, "commentEmployeeList", new ArrayList<>());

        // Act
        PerformanceResponse response = PerformanceAssembler.toResponseFromEntity(entity);

        // Assert
        assertThat(response.performanceId()).isEqualTo(23L);
        assertThat(response.employeeProfileId())
            .isEqualTo(ProfilePerformanceCommandFixtures.VALID_EMPLOYEE_PROFILE_ID);
        assertThat(response.dateTime()).isEqualTo(ProfilePerformanceCommandFixtures.VALID_PERFORMANCE_DATE);
        assertThat(response.classification()).isEqualTo(ProfilePerformanceCommandFixtures.VALID_CLASSIFICATION);
    }
}
