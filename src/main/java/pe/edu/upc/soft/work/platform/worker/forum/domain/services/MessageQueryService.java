package pe.edu.upc.soft.work.platform.worker.forum.domain.services;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Message;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetMessageByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAllMessageQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying Messages in the system.
 */
public interface MessageQueryService {

    /**
     * Retrieves a list of all Messages in the system.
     */
    List<Message> handle(GetAllMessageQuery query);

    /**
     * Retrieves a Message by their unique identifier.
     */
    Optional<Message> handle(GetMessageByIdQuery query);
}
