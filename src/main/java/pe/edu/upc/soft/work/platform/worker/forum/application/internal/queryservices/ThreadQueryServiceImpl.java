package pe.edu.upc.soft.work.platform.worker.forum.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Thread;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetThreadByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAllThreadQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetThreadsByAreaCompanyIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.ThreadQueryService;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.ThreadRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the ThreadQueryService interface.
 */
@Service
public class ThreadQueryServiceImpl implements ThreadQueryService {
    private final ThreadRepository threadRepository;

    /**
     * Constructor for ThreadQueryServiceImpl.
     */
    public ThreadQueryServiceImpl(ThreadRepository threadRepository) {
        this.threadRepository = threadRepository;
    }

    /**
     * Handles the GetAllThreadQuery.
     */
    @Override
    public List<Thread> handle(GetAllThreadQuery query) {
        return threadRepository.findAll();
    }

    /**
     * Handles the GetThreadByIdQuery.
     */
    @Override
    public Optional<Thread> handle(GetThreadByIdQuery query) {
        return threadRepository.findById(query.threadId());
    }

    @Override
    public List<Thread> handle(GetThreadsByAreaCompanyIdQuery query) {
        return threadRepository.findByAreaCompanyId(query.areaCompanyId());
    }
}
