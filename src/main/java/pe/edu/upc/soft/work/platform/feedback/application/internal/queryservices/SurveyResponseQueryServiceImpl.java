package pe.edu.upc.soft.work.platform.feedback.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.SurveyResponse;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetSurveyResponseByIdQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetAllSurveyResponseQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.services.SurveyResponseQueryService;
import pe.edu.upc.soft.work.platform.feedback.infrastructure.persistence.jpa.repositories.SurveyResponseRepository;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.SurveyResponseResponse;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the SurveyResponseQueryService interface.
 */
@Service
public class SurveyResponseQueryServiceImpl implements SurveyResponseQueryService {
    private final SurveyResponseRepository surveyresponseRepository;

    /**
     * Constructor for SurveyResponseQueryServiceImpl.
     */
    public SurveyResponseQueryServiceImpl(SurveyResponseRepository surveyresponseRepository) {
        this.surveyresponseRepository = surveyresponseRepository;
    }

    /**
     * Handles the GetAllSurveyResponseQuery.
     */
    @Override
    public List<SurveyResponseResponse> handle(GetAllSurveyResponseQuery query) {
        return surveyresponseRepository.findAll();
    }

    /**
     * Handles the GetSurveyResponseByIdQuery.
     */
    @Override
    public Optional<SurveyResponseResponse> handle(GetSurveyResponseByIdQuery query) {
        return surveyresponseRepository.findById(query.surveyresponseId());
    }
}
