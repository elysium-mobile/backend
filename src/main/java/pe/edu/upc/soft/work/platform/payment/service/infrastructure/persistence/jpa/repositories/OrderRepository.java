package pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.Order;

import java.util.List;

/**
 * Repository interface for managing Order entities.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserAccountId(Long userAccountId);
}
