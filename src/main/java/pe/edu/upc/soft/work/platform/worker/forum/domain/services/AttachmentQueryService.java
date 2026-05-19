package pe.edu.upc.soft.work.platform.worker.forum.domain.services;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Attachment;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAttachmentByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAllAttachmentQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying Attachments in the system.
 */
public interface AttachmentQueryService {

    /**
     * Retrieves a list of all Attachments in the system.
     */
    List<Attachment> handle(GetAllAttachmentQuery query);

    /**
     * Retrieves a Attachment by their unique identifier.
     */
    Optional<Attachment> handle(GetAttachmentByIdQuery query);
}
