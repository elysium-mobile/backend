package pe.edu.upc.soft.work.platform.feedback.domain.services;

import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.QuestionSurvey;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetAllQuestionSurveyQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetQuestionSurveyByIdQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetSurveyResponsesBySurveyIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying QuestionSurveys in the system.
 */
public interface QuestionSurveyQueryService {

    /**
     * Retrieves a list of all QuestionSurveys in the system.
     */
    List<QuestionSurvey> handle(GetAllQuestionSurveyQuery query);

    /**
     * Retrieves a QuestionSurvey by their unique identifier.
     */
    Optional<QuestionSurvey> handle(GetQuestionSurveyByIdQuery query);
}
