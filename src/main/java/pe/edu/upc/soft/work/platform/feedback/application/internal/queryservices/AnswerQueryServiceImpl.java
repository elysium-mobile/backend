package pe.edu.upc.soft.work.platform.feedback.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.Answer;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetAllAnswerQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetAnswerByIdQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.services.AnswerQueryService;
import pe.edu.upc.soft.work.platform.feedback.infrastructure.persistence.jpa.repositories.AnswerRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the AnswerQueryService interface.
 */
@Service
public class AnswerQueryServiceImpl implements AnswerQueryService {
    private final AnswerRepository answerRepository;

    /**
     * Constructor for AnswerQueryServiceImpl.
     */
    public AnswerQueryServiceImpl(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    /**
     * Handles the GetAllAnswerQuery.
     */
    @Override
    public List<Answer> handle(GetAllAnswerQuery query) {
        return answerRepository.findAll();
    }

    /**
     * Handles the GetAnswerByIdQuery.
     */
    @Override
    public Optional<Answer> handle(GetAnswerByIdQuery query) {
        return answerRepository.findById(query.answerId());
    }
}
