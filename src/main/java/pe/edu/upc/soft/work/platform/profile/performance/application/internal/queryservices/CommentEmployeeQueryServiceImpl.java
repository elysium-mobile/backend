package pe.edu.upc.soft.work.platform.profile.performance.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.aggregates.CommentEmployee;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.queries.GetCommentEmployeeByIdQuery;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.queries.GetAllCommentEmployeeQuery;
import pe.edu.upc.soft.work.platform.profile.performance.domain.services.CommentEmployeeQueryService;
import pe.edu.upc.soft.work.platform.profile.performance.infrastructure.persistence.jpa.repositories.CommentEmployeeRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the CommentEmployeeQueryService interface.
 */
@Service
public class CommentEmployeeQueryServiceImpl implements CommentEmployeeQueryService {
    private final CommentEmployeeRepository commentemployeeRepository;

    /**
     * Constructor for CommentEmployeeQueryServiceImpl.
     */
    public CommentEmployeeQueryServiceImpl(CommentEmployeeRepository commentemployeeRepository) {
        this.commentemployeeRepository = commentemployeeRepository;
    }

    /**
     * Handles the GetAllCommentEmployeeQuery.
     */
    @Override
    public List<CommentEmployee> handle(GetAllCommentEmployeeQuery query) {
        return commentemployeeRepository.findAll();
    }

    /**
     * Handles the GetCommentEmployeeByIdQuery.
     */
    @Override
    public Optional<CommentEmployee> handle(GetCommentEmployeeByIdQuery query) {
        return commentemployeeRepository.findById(query.commentemployeeId());
    }
}
