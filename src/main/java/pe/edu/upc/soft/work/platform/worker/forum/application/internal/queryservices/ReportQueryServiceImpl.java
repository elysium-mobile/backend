package pe.edu.upc.soft.work.platform.worker.forum.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Report;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAllReportsQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetReportByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.ReportQueryService;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.ReportRepository;

import java.util.List;
import java.util.Optional;


/**
 * Implementation of the ReportQueryService interface
 */
@Service
public class ReportQueryServiceImpl implements ReportQueryService {

    private final ReportRepository reportRepository;

    /**
     * Constructor for ReportQueryServiceImpl
     * @param reportRepository the report repository
     */
    public ReportQueryServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    /**
     * Handles the GetAllReportsQuery
     */
    @Override
    public List<Report> handle(GetAllReportsQuery query) {
        return reportRepository.findAll();
    }

    /**
     * Handles the GetReportByIdQuery
     */
    @Override
    public Optional<Report> handle(GetReportByIdQuery query) {
        return reportRepository.findById(query.reportId());
    }
}
