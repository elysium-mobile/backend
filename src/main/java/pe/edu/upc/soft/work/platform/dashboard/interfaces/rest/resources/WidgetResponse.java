package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

public record WidgetResponse(

        Long widgetId,
        String title,
        Integer refreshPeriod,
        Long dashboardId
) {
}
