package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateWidgetCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateWidgetCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.Widget;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.CreateWidgetRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.UpdateWidgetRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.WidgetResponse;

public class WidgetAssembler {

    /**
     *  Converts a CreateWidgetRequest to a CreateWidgetCommand.
     */
    public static CreateWidgetCommand toCommandFromRequest(CreateWidgetRequest request){
        return new CreateWidgetCommand(request.title(), request.refreshPeriod());
    }

    /**
     *  Converts an UpdateWidgetRequest to an UpdateWidgetCommand.
     */
    public static UpdateWidgetCommand toCommandFromRequest(Long widgetId, UpdateWidgetRequest request){
        return new UpdateWidgetCommand(widgetId, request.title(), request.refreshPeriod());
    }

    /**
     *  Converts a Widget entity to a WidgetResponse.
     */
    public static WidgetResponse toResponseFromEntity(Widget widget){
        return new WidgetResponse(widget.getId(), widget.getTitle(), widget.getRefreshPeriod());
    }
}
