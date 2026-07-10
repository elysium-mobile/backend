package pe.edu.upc.soft.work.platform.dashboard.domain.services;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.AnalyzeDashboardCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.valueObjects.DashboardInsight;

/**
 * Service interface for analyzing a company's dashboard/climate metrics using AI.
 */
public interface DashboardAssistantService {

  /**
   * Handles a request to analyze the dashboard metrics of a company.
   *
   * @param command the command containing the company ID and an optional follow-up question
   * @return a DashboardInsight with a diagnosis, an AI-generated analysis and the raw metrics used
   */
  DashboardInsight handle(AnalyzeDashboardCommand command);
}
