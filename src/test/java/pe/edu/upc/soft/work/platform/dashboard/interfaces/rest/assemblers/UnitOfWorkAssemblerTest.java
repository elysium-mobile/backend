package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateUnitOfWorkCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateUnitOfWorkCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.UnitOfWork;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.CreateUnitOfWorkRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.UnitOfWorkResponse;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.UpdateUnitOfWorkRequest;
import pe.edu.upc.soft.work.platform.dashboard.test.fixtures.DashboardCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class UnitOfWorkAssemblerTest {

    @Test
    @DisplayName("toCommandFromRequest(CreateUnitOfWorkRequest) -> maps name to CreateUnitOfWorkCommand (AAA)")
    void toCommandFromCreateRequestMapsName() {
        // Arrange
        var request = new CreateUnitOfWorkRequest(DashboardCommandFixtures.VALID_UNIT_OF_WORK_NAME);

        // Act
        CreateUnitOfWorkCommand command = UnitOfWorkAssembler.toCommandFromRequest(request);

        // Assert
        assertThat(command.name()).isEqualTo(DashboardCommandFixtures.VALID_UNIT_OF_WORK_NAME);
    }

    @Test
    @DisplayName("toCommandFromRequest(Long, UpdateUnitOfWorkRequest) -> maps id and name to UpdateUnitOfWorkCommand (AAA)")
    void toCommandFromUpdateRequestMapsAllFields() {
        // Arrange
        var request = new UpdateUnitOfWorkRequest(DashboardCommandFixtures.VALID_UNIT_OF_WORK_NAME);

        // Act
        UpdateUnitOfWorkCommand command = UnitOfWorkAssembler.toCommandFromRequest(33L, request);

        // Assert
        assertThat(command.unitofworkId()).isEqualTo(33L);
        assertThat(command.name()).isEqualTo(DashboardCommandFixtures.VALID_UNIT_OF_WORK_NAME);
    }

    @Test
    @DisplayName("toResponseFromEntity(UnitOfWork) -> maps id and name to UnitOfWorkResponse (AAA)")
    void toResponseFromEntityMapsAllFields() {
        // Arrange
        var entity = new UnitOfWork(DashboardCommandFixtures.validCreateUnitOfWorkCommand());
        ReflectionTestUtils.setId(entity, 33L);

        // Act
        UnitOfWorkResponse response = UnitOfWorkAssembler.toResponseFromEntity(entity);

        // Assert
        assertThat(response.unitOfWorkId()).isEqualTo(33L);
        assertThat(response.name()).isEqualTo(DashboardCommandFixtures.VALID_UNIT_OF_WORK_NAME);
    }
}
