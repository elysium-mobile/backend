package pe.edu.upc.soft.work.platform.worker.forum.domain.services;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Thread;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetThreadByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAllThreadQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying Threads in the system.
 */
public interface ThreadQueryService {

    /**
     * Retrieves a list of all Threads in the system.
     */
    List<Thread> handle(GetAllThreadQuery query);

    /**
     * Retrieves a Thread by their unique identifier.
     */
    Optional<Thread> handle(GetThreadByIdQuery query);
}
