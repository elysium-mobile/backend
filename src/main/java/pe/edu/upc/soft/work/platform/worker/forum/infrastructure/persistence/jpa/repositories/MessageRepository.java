package pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Message;

import java.util.List;

/**
 * Repository interface for managing Message entities.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

  List<Message> findByUserAccountId(Long userAccountId);

  List<Message> findByThreadId(Long threadId);
}
