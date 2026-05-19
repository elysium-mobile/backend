package pe.edu.upc.soft.work.platform.worker.forum.domain.services;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Forum;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetForumByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAllForumQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying Forums in the system.
 */
public interface ForumQueryService {

    /**
     * Retrieves a list of all Forums in the system.
     */
    List<Forum> handle(GetAllForumQuery query);

    /**
     * Retrieves a Forum by their unique identifier.
     */
    Optional<Forum> handle(GetForumByIdQuery query);
}
