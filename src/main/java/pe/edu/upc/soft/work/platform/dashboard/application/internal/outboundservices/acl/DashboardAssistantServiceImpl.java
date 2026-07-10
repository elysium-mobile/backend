package pe.edu.upc.soft.work.platform.dashboard.application.internal.outboundservices.acl;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.AnalyzeDashboardCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAveragePerformanceByCompanyQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetForumActivityByCompanyQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetPositiveSurveyRateByCompanyQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetReportCountByCompanyQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.valueObjects.DashboardInsight;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.DashboardAssistantService;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.DashboardQueryService;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Service that examines a company's dashboard metrics (performance, survey
 * sentiment, reports and forum activity) and uses AI (Gemini) to produce a
 * human-readable diagnosis of the work climate along with actionable
 * recommendations for RRHH.
 *
 * <p>The "BUENO / REGULAR / CRITICO" status is computed deterministically from
 * the metrics themselves (not by the AI) so it stays consistent; the AI is
 * only used to explain the reasoning and to suggest recommendations.</p>
 */
@Service
public class DashboardAssistantServiceImpl implements DashboardAssistantService {

  private static final double GOOD_PERFORMANCE_THRESHOLD = 3.5;
  private static final double GOOD_POSITIVE_RATE_THRESHOLD = 70.0;
  private static final double CRITICAL_PERFORMANCE_THRESHOLD = 2.5;
  private static final double CRITICAL_POSITIVE_RATE_THRESHOLD = 40.0;
  private static final int POSITIVE_SURVEY_THRESHOLD = 3;

  /**
   * AI chat client tailored with a dashboard/climate-analysis persona.
   */
  private final ChatClient chatClient;

  /**
   * Query service used to gather the dashboard metrics of a company.
   */
  private final DashboardQueryService dashboardQueryService;

  /**
   * Constructor for DashboardAssistantServiceImpl.
   *
   * @param chatClient            the AI chat client instance (dashboard assistant persona)
   * @param dashboardQueryService the dashboard query service instance
   */
  public DashboardAssistantServiceImpl(
      @Qualifier("dashboardAssistantChatClient") ChatClient chatClient,
      DashboardQueryService dashboardQueryService) {
    this.chatClient = chatClient;
    this.dashboardQueryService = dashboardQueryService;
  }

  /**
   * Handles the request to analyze a company's dashboard.
   * Gathers the relevant metrics, computes a deterministic status label and
   * asks the AI to explain it and provide recommendations.
   *
   * @param command the command containing the company ID and optional follow-up question
   * @return a DashboardInsight with the status, AI analysis and raw metrics
   * @throws RuntimeException if an error occurs during AI response generation
   */
  @Override
  public DashboardInsight handle(AnalyzeDashboardCommand command) {
    Long companyId = command.companyId();

    var performance = dashboardQueryService.handle(new GetAveragePerformanceByCompanyQuery(companyId));
    var positiveSurvey = dashboardQueryService.handle(
        new GetPositiveSurveyRateByCompanyQuery(companyId, POSITIVE_SURVEY_THRESHOLD));
    var reports = dashboardQueryService.handle(new GetReportCountByCompanyQuery(companyId));
    var forumActivity = dashboardQueryService.handle(new GetForumActivityByCompanyQuery(companyId));

    double averagePerformance = toDouble(performance.get("average"));
    double positiveRate = toDouble(positiveSurvey.get("positiveRate"));
    long totalReports = toLong(reports.get("totalReports"));
    long totalMessages = toLong(forumActivity.get("totalMessages"));

    String status = computeStatus(averagePerformance, positiveRate);

    Map<String, Object> metrics = new LinkedHashMap<>();
    metrics.put("averagePerformance", averagePerformance);
    metrics.put("totalEvaluations", performance.get("totalEvaluations"));
    metrics.put("positiveSurveyRate", positiveRate);
    metrics.put("totalSurveyAnswers", positiveSurvey.get("totalAnswers"));
    metrics.put("totalReports", totalReports);
    metrics.put("reportsByArea", reports.get("byArea"));
    metrics.put("totalForumMessages", totalMessages);
    metrics.put("forumActivityByArea", forumActivity.get("byArea"));

    String prompt = buildPrompt(companyId, status, averagePerformance, positiveRate,
        totalReports, totalMessages, reports.get("byArea"), forumActivity.get("byArea"),
        command.question());

    try {
      var response = chatClient.prompt()
          .user(u -> u.text(prompt))
          .call()
          .content();
      return new DashboardInsight(status, response, metrics);
    } catch (Exception e) {
      throw new RuntimeException("Error generating dashboard analysis: " + e.getMessage(), e);
    }
  }

  private String buildPrompt(Long companyId, String status, double averagePerformance, double positiveRate,
                              long totalReports, long totalMessages, Object reportsByArea,
                              Object forumActivityByArea, String question) {
    var prompt = new StringBuilder();
    prompt.append("Diagnóstico preliminar calculado por el sistema: ").append(status).append(". ");
    prompt.append("Datos del dashboard de la empresa (id=").append(companyId).append("): ");
    prompt.append("Desempeño promedio de los empleados: ").append(averagePerformance).append(" sobre 5. ");
    prompt.append("Tasa de encuestas de clima positivas: ").append(positiveRate).append("%. ");
    prompt.append("Total de reportes/incidencias registrados: ").append(totalReports).append(". ");
    prompt.append("Distribución de reportes por área: ").append(reportsByArea).append(". ");
    prompt.append("Total de mensajes en el foro de trabajadores: ").append(totalMessages).append(". ");
    prompt.append("Actividad del foro por área: ").append(forumActivityByArea).append(". ");

    if (question != null && !question.isBlank()) {
      prompt.append("Pregunta específica de RRHH sobre estos datos: ").append(question);
    } else {
      prompt.append("Analiza cómo está el ambiente laboral en base a estos datos y da recomendaciones.");
    }
    return prompt.toString();
  }

  private String computeStatus(double averagePerformance, double positiveRate) {
    if (averagePerformance >= GOOD_PERFORMANCE_THRESHOLD && positiveRate >= GOOD_POSITIVE_RATE_THRESHOLD) {
      return "BUENO";
    }
    if (averagePerformance < CRITICAL_PERFORMANCE_THRESHOLD || positiveRate < CRITICAL_POSITIVE_RATE_THRESHOLD) {
      return "CRITICO";
    }
    return "REGULAR";
  }

  private double toDouble(Object value) {
    return value instanceof Number number ? number.doubleValue() : 0.0;
  }

  private long toLong(Object value) {
    return value instanceof Number number ? number.longValue() : 0L;
  }
}
