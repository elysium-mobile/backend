package pe.edu.upc.soft.work.platform.feedback.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.QuestionSurvey;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetAllQuestionSurveyQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.model.queries.GetQuestionSurveyByIdQuery;
import pe.edu.upc.soft.work.platform.feedback.domain.services.QuestionSurveyQueryService;
import pe.edu.upc.soft.work.platform.feedback.infrastructure.persistence.jpa.repositories.QuestionSurveyRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the QuestionSurveyQueryService interface.
 */
@Service
public class QuestionSurveyQueryServiceImpl implements QuestionSurveyQueryService {
    private final QuestionSurveyRepository questionsurveyRepository;

    /**
     * Constructor for QuestionSurveyQueryServiceImpl.
     */
    public QuestionSurveyQueryServiceImpl(QuestionSurveyRepository questionsurveyRepository) {
        this.questionsurveyRepository = questionsurveyRepository;
    }

    /**
     * Handles the GetAllQuestionSurveyQuery.
     */
    @Override
    public List<QuestionSurvey> handle(GetAllQuestionSurveyQuery query) {
        return questionsurveyRepository.findAll();
    }

    /**
     * Handles the GetQuestionSurveyByIdQuery.
     */
    @Override
    public Optional<QuestionSurvey> handle(GetQuestionSurveyByIdQuery query) {
        return questionsurveyRepository.findById(query.questionsurveyId());
    }
}
