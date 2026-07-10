package pe.edu.upc.soft.work.platform.payment.service.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.Order;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetOrderByIdQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetAllOrderQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetOrderByUserAccountIdQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.OrderQueryService;
import pe.edu.upc.soft.work.platform.payment.service.infrastructure.persistence.jpa.repositories.OrderRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the OrderQueryService interface.
 */
@Service
public class OrderQueryServiceImpl implements OrderQueryService {
    private final OrderRepository orderRepository;

    /**
     * Constructor for OrderQueryServiceImpl.
     */
    public OrderQueryServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Handles the GetAllOrderQuery.
     */
    @Override
    public List<Order> handle(GetAllOrderQuery query) {
        return orderRepository.findAll();
    }

    /**
     * Handles the GetOrderByIdQuery.
     */
    @Override
    public Optional<Order> handle(GetOrderByIdQuery query) {
        return orderRepository.findById(query.orderId());
    }

    @Override
    public List<Order> handle(GetOrderByUserAccountIdQuery query) {
        return orderRepository.findByUserAccountId_UserAccountId(query.userAccountId());
    }
}
