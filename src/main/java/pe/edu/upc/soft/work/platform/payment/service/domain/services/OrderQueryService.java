package pe.edu.upc.soft.work.platform.payment.service.domain.services;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.Order;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetOrderByIdQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetAllOrderQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetOrderByUserAccountIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying Orders in the system.
 */
public interface OrderQueryService {

    /**
     * Retrieves a list of all Orders in the system.
     */
    List<Order> handle(GetAllOrderQuery query);

    /**
     * Retrieves a Order by their unique identifier.
     */
    Optional<Order> handle(GetOrderByIdQuery query);

    List<Order> handle(GetOrderByUserAccountIdQuery query);
}
