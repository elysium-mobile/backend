package pe.edu.upc.soft.work.platform.dashboard.domain.model.commands;

/**
 * Command to ask the Dashboard Assistant to analyze the current climate metrics
 * of a company and provide a diagnosis with recommendations.
 *
 * @param companyId the ID of the company whose dashboard metrics will be analyzed
 * @param question  optional, specific follow-up question from RRHH about the metrics
 */
public record AnalyzeDashboardCommand(Long companyId, String question) {

  public AnalyzeDashboardCommand {
    if (companyId == null) {
      throw new IllegalArgumentException("Company id must not be null.");
    }
  }
}
