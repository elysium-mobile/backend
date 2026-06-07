package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates.Dashboard;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateDashboardCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateDashboardCommand;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.CreateDashboardRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.UpdateDashboardRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.DashboardResponse;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.WidgetResponse;

import java.util.ArrayList;
import java.util.List;

public class DashboardAssembler {

    /**
     * Converts a CreateDashboardRequest to a CreateDashboardCommand.
     */
    public static CreateDashboardCommand toCommandFromRequest(CreateDashboardRequest request) {
        return new CreateDashboardCommand(request.ruc(), request.title(), request.description(), 0L,new ArrayList<>());
    }

    /**
     * Converts an UpdateDashboardRequest to an UpdateDashboardCommand.
     */
    public static UpdateDashboardCommand toCommandFromRequest(Long dashboardId, UpdateDashboardRequest request) {
        return new UpdateDashboardCommand(dashboardId,request.title(),request.description(), request.ruc(), request.companyId());
    }

    /**
     * Converts a Dashboard entity to a DashboardResponse.
     */
    public static DashboardResponse toResponseFromEntity(Dashboard dashboard) {
        List<WidgetResponse> widgetResponses = dashboard.getWidgets().stream()
                .map(widget -> new WidgetResponse(
                        widget.getId(),
                        widget.getTitle(),
                        widget.getRefreshPeriod(),
                        widget.getDashboardId()
                ))
                .toList();
        return new DashboardResponse(dashboard.getId(),dashboard.getTitle(), dashboard.getDescription(), dashboard.getRuc(),dashboard.getCompanyId(),widgetResponses);
    }
}
