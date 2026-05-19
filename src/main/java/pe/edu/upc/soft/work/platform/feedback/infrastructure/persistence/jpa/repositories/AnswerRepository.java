package pe.edu.upc.soft.work.platform.feedback.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.feedback.domain.model.entities.Answer;

/**
 * Repository interface for managing Answer entities.
 */
@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
