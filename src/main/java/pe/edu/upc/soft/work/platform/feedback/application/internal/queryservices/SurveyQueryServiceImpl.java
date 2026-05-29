package pe.edu.upc.soft.work.platform.feedback.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.feedback.domain.model.aggregates.Survey;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetAllSurveyQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetSurveyByIdQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.services.SurveyQueryService;
import pe.edu.upc.soft.work.platform.feedback.infrastructure.persistence.jpa.repositories.SurveyRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the SurveyQueryService interface.
 */
@Service
public class SurveyQueryServiceImpl implements SurveyQueryService {
    private final SurveyRepository surveyRepository;

    /**
     * Constructor for SurveyQueryServiceImpl.
     */
    public SurveyQueryServiceImpl(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    /**
     * Handles the GetAllSurveyQuery.
     */
    @Override
    public List<Survey> handle(GetAllSurveyQuery query) {
        return surveyRepository.findAll();
    }

    /**
     * Handles the GetSurveyByIdQuery.
     */
    @Override
    public Optional<Survey> handle(GetSurveyByIdQuery query) {
        return surveyRepository.findById(query.surveyId());
    }
}
