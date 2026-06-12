package pe.edu.upc.soft.work.platform.feedback.domain.services;

import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.Answer;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetAllAnswerQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetAnswerByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying Answers in the system.
 */
public interface AnswerQueryService {

    /**
     * Retrieves a list of all Answers in the system.
     */
    List<Answer> handle(GetAllAnswerQuery query);

    /**
     * Retrieves a Answer by their unique identifier.
     */
    Optional<Answer> handle(GetAnswerByIdQuery query);
}
