package pe.edu.upc.soft.work.platform.worker.forum.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Forum;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetForumByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAllForumQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetForumsByCompanyIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.ForumQueryService;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.ForumRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the ForumQueryService interface.
 */
@Service
public class ForumQueryServiceImpl implements ForumQueryService {
    private final ForumRepository forumRepository;

    /**
     * Constructor for ForumQueryServiceImpl.
     */
    public ForumQueryServiceImpl(ForumRepository forumRepository) {
        this.forumRepository = forumRepository;
    }

    /**
     * Handles the GetAllForumQuery.
     */
    @Override
    public List<Forum> handle(GetAllForumQuery query) {
        return forumRepository.findAll();
    }

    /**
     * Handles the GetForumByIdQuery.
     */
    @Override
    public Optional<Forum> handle(GetForumByIdQuery query) {
        return forumRepository.findById(query.forumId());
    }

    @Override
    public List<Forum> handle(GetForumsByCompanyIdQuery query) {
        return forumRepository.findByCompanyId(query.companyId());
    }
}
