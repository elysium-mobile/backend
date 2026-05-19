package pe.edu.upc.soft.work.platform.feedback.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.feedback.domain.model.aggregates.Survey;

/**
 * Repository interface for managing Survey entities.
 */
@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {
}
