package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.AnalyzeDashboardCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.valueObjects.DashboardInsight;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.AnalyzeDashboardRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.DashboardInsightResponse;

/**
 * Assembler for converting between REST resources and domain objects
 * of the Dashboard Assistant feature.
 */
public class DashboardAssistantAssembler {

  public static AnalyzeDashboardCommand toCommandFromRequest(AnalyzeDashboardRequest request) {
    return new AnalyzeDashboardCommand(request.companyId(), request.question());
  }

  public static DashboardInsightResponse toResponseFromEntity(DashboardInsight insight) {
    return new DashboardInsightResponse(insight.status(), insight.analysis(), insight.metrics());
  }
}
