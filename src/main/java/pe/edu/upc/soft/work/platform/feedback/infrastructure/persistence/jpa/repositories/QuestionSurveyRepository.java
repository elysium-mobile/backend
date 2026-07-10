package pe.edu.upc.soft.work.platform.feedback.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.QuestionSurvey;

import java.util.List;

/**
 * Repository interface for managing QuestionSurvey entities.
 */
@Repository
public interface QuestionSurveyRepository extends JpaRepository<QuestionSurvey, Long> {

    boolean existsQuestionSurveyById(Long questionSurveyId);

    List<QuestionSurvey> findBySurveyIdIn(List<Long> surveyIds);
}
