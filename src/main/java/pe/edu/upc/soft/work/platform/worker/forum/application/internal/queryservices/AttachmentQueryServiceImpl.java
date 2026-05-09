package pe.edu.upc.soft.work.platform.worker.forum.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Attachment;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAttachmentByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAllAttachmentQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.AttachmentQueryService;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.AttachmentRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the AttachmentQueryService interface.
 */
@Service
public class AttachmentQueryServiceImpl implements AttachmentQueryService {
    private final AttachmentRepository attachmentRepository;

    /**
     * Constructor for AttachmentQueryServiceImpl.
     */
    public AttachmentQueryServiceImpl(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    /**
     * Handles the GetAllAttachmentQuery.
     */
    @Override
    public List<Attachment> handle(GetAllAttachmentQuery query) {
        return attachmentRepository.findAll();
    }

    /**
     * Handles the GetAttachmentByIdQuery.
     */
    @Override
    public Optional<Attachment> handle(GetAttachmentByIdQuery query) {
        return attachmentRepository.findById(query.attachmentId());
    }
}
