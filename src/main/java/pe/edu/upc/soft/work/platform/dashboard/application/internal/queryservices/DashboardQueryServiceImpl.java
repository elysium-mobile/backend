package pe.edu.upc.soft.work.platform.dashboard.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.dashboard.application.internal.outboundservices.acl.ExternalIamServiceFromDashboard;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates.Dashboard;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.AreaCompany;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.*;
import pe.edu.upc.soft.work.platform.dashboard.domain.services.DashboardQueryService;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.AreaCompanyRepository;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.CompanyRepository;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.DashboardRepository;
import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.Answer;
import pe.edu.upc.soft.work.platform.feedback.infrastructure.persistence.jpa.repositories.AnswerRepository;
import pe.edu.upc.soft.work.platform.feedback.infrastructure.persistence.jpa.repositories.QuestionSurveyRepository;
import pe.edu.upc.soft.work.platform.feedback.infrastructure.persistence.jpa.repositories.SurveyRepository;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.aggregates.Performance;
import pe.edu.upc.soft.work.platform.profile.performance.infrastructure.persistence.jpa.repositories.PerformanceRepository;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Report;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Thread;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.ReportRepository;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.ThreadRepository;

import java.util.*;

/**
 * Implementation of the DashboardQueryService interface.
 */
@Service
public class DashboardQueryServiceImpl implements DashboardQueryService {
    private final DashboardRepository dashboardRepository;
    private final CompanyRepository companyRepository;
    private final AreaCompanyRepository areaCompanyRepository;
    private final PerformanceRepository performanceRepository;
    private final SurveyRepository surveyRepository;
    private final AnswerRepository answerRepository;
    private final QuestionSurveyRepository questionSurveyRepository;
    private final ReportRepository reportRepository;
    private final ThreadRepository threadRepository;
    private final ExternalIamServiceFromDashboard externalIamService;

    public DashboardQueryServiceImpl(
        DashboardRepository dashboardRepository,
        CompanyRepository companyRepository,
        AreaCompanyRepository areaCompanyRepository,
        PerformanceRepository performanceRepository,
        SurveyRepository surveyRepository,
        AnswerRepository answerRepository,
        QuestionSurveyRepository questionSurveyRepository,
        ReportRepository reportRepository,
        ThreadRepository threadRepository,
        ExternalIamServiceFromDashboard externalIamService) {
        this.dashboardRepository = dashboardRepository;
        this.companyRepository = companyRepository;
        this.areaCompanyRepository = areaCompanyRepository;
        this.performanceRepository = performanceRepository;
        this.surveyRepository = surveyRepository;
        this.answerRepository = answerRepository;
        this.questionSurveyRepository = questionSurveyRepository;
        this.reportRepository = reportRepository;
        this.threadRepository = threadRepository;
        this.externalIamService = externalIamService;
    }

    /**
     * Handles the GetAllDashboardQuery.
     * @return a list of all dashboards.
     */
    @Override
    public List<Dashboard> handle(GetAllDashboardQuery query) {
        return dashboardRepository.findAll();
    }

    /**
     * Handles the GetDashboardByIdQuery.
     * @param query the query containing the dashboard ID.
     * @return an optional containing the dashboard if found, otherwise empty.
     */
    @Override
    public Optional<Dashboard> handle(GetDashboardByIdQuery query) {
        return dashboardRepository.findById(query.dashboardId());
    }

    /**
     * Handles the GetDashboardByCompanyIdQuery.
     * @param query the query containing the company ID.
     * @return a list of dashboards associated with the specified company.
     */
    @Override
    public List<Dashboard> handle(GetDashboardByCompanyIdQuery query) {
        return this.dashboardRepository.findByCompanyId(query.companyId());
    }

    /**
     * Handles the GetAveragePerformanceByCompanyQuery.
     * Calculates the average performance classification for employees of a specific company.
     * @param query the query containing the company ID.
     * @return a map containing companyId, average performance, and total evaluation count.
     */
    @Override
    public Map<String, Object> handle(GetAveragePerformanceByCompanyQuery query) {
        var company = companyRepository.findById(query.companyId())
            .orElseThrow(() -> new NoSuchElementException("Company not found: " + query.companyId()));

        List<Long> userAccountIds = company.getEmployees() == null ? List.of()
            : company.getEmployees().stream().map(ua -> ua.getId()).toList();

        List<Long> profileIds = externalIamService.getEmployeeProfileIdsByUserAccountIds(userAccountIds);

        List<Performance> performances = profileIds.isEmpty()
            ? List.of()
            : performanceRepository.findByEmployeeProfileIdEmployeeProfileIdIn(profileIds);

        OptionalDouble avg = performances.stream()
            .map(Performance::getClassification)
            .filter(Objects::nonNull)
            .mapToInt(Integer::intValue)
            .average();

        return Map.of(
            "companyId", query.companyId(),
            "average", avg.orElse(0.0),
            "totalEvaluations", (long) performances.size()
        );
    }

    /**
     * Handles the GetPositiveSurveyRateByCompanyQuery.
     * Calculates the rate of positive survey answers based on a defined threshold.
     * @param query the query containing company ID and positive threshold.
     * @return a map with companyId, positive rate percentage, count of positive answers, total answers, and the threshold used.
     */
    @Override
    public Map<String, Object> handle(GetPositiveSurveyRateByCompanyQuery query) {
        List<Long> surveyIds = surveyRepository.findAll().stream()
            .map(s -> s.getId()).toList();

        List<Long> questionSurveyIds = surveyIds.isEmpty()
            ? List.of()
            : questionSurveyRepository.findBySurveyIdIn(surveyIds).stream()
                .map(qs -> qs.getId()).toList();

        List<Answer> answers = questionSurveyIds.isEmpty()
            ? List.of()
            : answerRepository.findByValueIn(questionSurveyIds);

        long total = answers.size();
        long positive = answers.stream()
            .filter(a -> a.getScoreAnswer() != null && a.getScoreAnswer() >= query.positiveThreshold())
            .count();

        double rate = total == 0 ? 0.0 : Math.round((positive * 100.0 / total) * 100.0) / 100.0;

        return Map.of(
            "companyId", query.companyId(),
            "positiveRate", rate,
            "positiveCount", positive,
            "totalAnswers", total,
            "threshold", query.positiveThreshold()
        );
    }

    /**
     * Handles the GetReportCountByCompanyQuery.
     * Retrieves the total count of reports and the distribution of reports by area for a company.
     * @param query the query containing the company ID.
     * @return a map with companyId, total report count, and a list of reports grouped by area.
     */
    @Override
    public Map<String, Object> handle(GetReportCountByCompanyQuery query) {
        List<AreaCompany> areas = areaCompanyRepository.findByCompanyId(query.companyId());
        List<Long> areaIds = areas.stream().map(a -> a.getId()).toList();

        List<Report> reports = areaIds.isEmpty()
            ? List.of()
            : reportRepository.findByAreaCompanyIdAreaCompanyIdIn(areaIds);

        Map<Long, Long> countByAreaId = new HashMap<>();
        reports.forEach(r -> countByAreaId.merge(
            r.getAreaCompanyId().areaCompanyId(), 1L, Long::sum));

        List<Map<String, Object>> byArea = areas.stream().map(area -> {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("areaId", area.getId());
            entry.put("areaName", area.getName());
            entry.put("reportCount", countByAreaId.getOrDefault(area.getId(), 0L));
            return entry;
        }).toList();

        return Map.of(
            "companyId", query.companyId(),
            "totalReports", (long) reports.size(),
            "byArea", byArea
        );
    }

    /**
     * Handles the GetForumActivityByCompanyQuery.
     * Retrieves forum engagement statistics (threads and messages) grouped by company area.
     * @param query the query containing the company ID.
     * @return a map with companyId, total threads, total messages, and detailed stats by area.
     */
    @Override
    public Map<String, Object> handle(GetForumActivityByCompanyQuery query) {
        List<AreaCompany> areas = areaCompanyRepository.findByCompanyId(query.companyId());
        List<Long> areaIds = areas.stream().map(a -> a.getId()).toList();

        List<Thread> threads = areaIds.isEmpty()
            ? List.of()
            : threadRepository.findByAreaCompanyIdAreaCompanyIdIn(areaIds);

        Map<Long, long[]> statsByArea = new HashMap<>();
        threads.forEach(t -> {
            Long areaId = t.getAreaCompanyId().areaCompanyId();
            long msgs = t.getMessageCount() != null ? t.getMessageCount() : 0L;
            statsByArea.compute(areaId, (k, v) -> {
                if (v == null) v = new long[]{0L, 0L};
                v[0]++;
                v[1] += msgs;
                return v;
            });
        });

        long totalThreads = threads.size();
        long totalMessages = threads.stream()
            .mapToLong(t -> t.getMessageCount() != null ? t.getMessageCount() : 0L).sum();

        List<Map<String, Object>> byArea = areas.stream().map(area -> {
            long[] stats = statsByArea.getOrDefault(area.getId(), new long[]{0L, 0L});
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("areaId", area.getId());
            entry.put("areaName", area.getName());
            entry.put("threadCount", stats[0]);
            entry.put("messageCount", stats[1]);
            return entry;
        }).toList();

        return Map.of(
            "companyId", query.companyId(),
            "totalThreads", totalThreads,
            "totalMessages", totalMessages,
            "byArea", byArea
        );
    }

    /**
     * Handles the GetEmployeeCountByAreaQuery.
     * Calculates the total number of employees and distributes them by area within the company.
     * @param query the query containing the company ID.
     * @return a map with companyId, total employee count, and employee count per area.
     */
    @Override
    public Map<String, Object> handle(GetEmployeeCountByAreaQuery query) {
        List<AreaCompany> areas = areaCompanyRepository.findByCompanyId(query.companyId());

        long totalEmployees = 0L;
        List<Map<String, Object>> byArea = new ArrayList<>();

        for (var area : areas) {
            List<Long> workTeamIds = new ArrayList<>();
            if (area.getUnitOfWorkList() != null) {
                area.getUnitOfWorkList().forEach(unit -> {
                    if (unit.getWorkTeamList() != null) {
                        unit.getWorkTeamList().forEach(wt -> workTeamIds.add(wt.getId()));
                    }
                });
            }
            long count = externalIamService.countEmployeeProfilesByWorkTeamIds(workTeamIds);
            totalEmployees += count;

            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("areaId", area.getId());
            entry.put("areaName", area.getName());
            entry.put("employeeCount", count);
            byArea.add(entry);
        }

        return Map.of(
            "companyId", query.companyId(),
            "totalEmployees", totalEmployees,
            "byArea", byArea
        );
    }
}
