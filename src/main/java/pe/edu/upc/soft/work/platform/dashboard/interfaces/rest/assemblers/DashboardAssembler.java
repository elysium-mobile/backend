package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates.Dashboard;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateDashboardCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateDashboardCommand;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.CreateDashboardRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.UpdateDashboardRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.DashboardResponse;

public class DashboardAssembler {

    /**
     * Converts a CreateDashboardRequest to a CreateDashboardCommand.
     */
    public static CreateDashboardCommand toCommandFromRequest(CreateDashboardRequest request) {
        return new CreateDashboardCommand(request.ruc());
    }

    /**
     * Converts an UpdateDashboardRequest to an UpdateDashboardCommand.
     */
    public static UpdateDashboardCommand toCommandFromRequest(Long dashboardId, UpdateDashboardRequest request) {
        return new UpdateDashboardCommand(dashboardId, request.ruc());
    }

    /**
     * Converts a Dashboard entity to a DashboardResponse.
     */
    public static DashboardResponse toResponseFromEntity(Dashboard dashboard) {
        return new DashboardResponse(dashboard.getId(), dashboard.getRuc());
    }
}
