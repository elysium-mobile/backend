package pe.edu.upc.soft.work.platform.feedback.domain.services;

import pe.edu.upc.soft.work.platform.feedback.domain.model.aggregates.Survey;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetSurveyByIdQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetAllSurveyQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying Surveys in the system.
 */
public interface SurveyQueryService {

    /**
     * Retrieves a list of all Surveys in the system.
     */
    List<Survey> handle(GetAllSurveyQuery query);

    /**
     * Retrieves a Survey by their unique identifier.
     */
    Optional<Survey> handle(GetSurveyByIdQuery query);
}
