package pe.edu.upc.soft.work.platform.feedback.domain.services;

import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.QuestionSurvey;
import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.SurveyResponse;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetAllSurveyResponseQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetSurveyResponseByIdQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetSurveyResponsesBySurveyIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying SurveyResponses in the system.
 */
public interface SurveyResponseQueryService {

    /**
     * Retrieves a list of all SurveyResponses in the system.
     */
    List<SurveyResponse> handle(GetAllSurveyResponseQuery query);

    /**
     * Retrieves a SurveyResponse by their unique identifier.
     */
    Optional<SurveyResponse> handle(GetSurveyResponseByIdQuery query);

    List<SurveyResponse> handle(GetSurveyResponsesBySurveyIdQuery query);
}
