package pe.edu.upc.soft.work.platform.profile.performance.domain.services;

import pe.edu.upc.soft.work.platform.profile.performance.domain.model.aggregates.CommentEmployee;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.queries.GetCommentEmployeeByIdQuery;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.queries.GetAllCommentEmployeeQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying CommentEmployees in the system.
 */
public interface CommentEmployeeQueryService {

    /**
     * Retrieves a list of all CommentEmployees in the system.
     */
    List<CommentEmployee> handle(GetAllCommentEmployeeQuery query);

    /**
     * Retrieves a CommentEmployee by their unique identifier.
     */
    Optional<CommentEmployee> handle(GetCommentEmployeeByIdQuery query);
}
