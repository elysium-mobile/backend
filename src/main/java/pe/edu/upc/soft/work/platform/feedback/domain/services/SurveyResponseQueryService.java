package pe.edu.upc.soft.work.platform.feedback.domain.services;

import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.SurveyResponse;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetSurveyResponseByIdQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetAllSurveyResponseQuery;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.SurveyResponseResponse;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying SurveyResponses in the system.
 */
public interface SurveyResponseQueryService {

    /**
     * Retrieves a list of all SurveyResponses in the system.
     */
    List<SurveyResponseResponse> handle(GetAllSurveyResponseQuery query);

    /**
     * Retrieves a SurveyResponse by their unique identifier.
     */
    Optional<SurveyResponseResponse> handle(GetSurveyResponseByIdQuery query);
}
