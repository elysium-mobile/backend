package pe.edu.upc.soft.work.platform.feedback.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.SurveyResponse;
import pe.edu.upc.soft.work.platform.feedback.interfaces.rest.resources.SurveyResponseResponse;

/**
 * Repository interface for managing SurveyResponse entities.
 */
@Repository
public interface SurveyResponseRepository extends JpaRepository<SurveyResponseResponse, Long> {
}
