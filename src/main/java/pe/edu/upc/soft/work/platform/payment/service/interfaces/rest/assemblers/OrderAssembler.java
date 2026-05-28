package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateOrderCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateOrderCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.entities.Order;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects.UserAccountId;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.CreateOrderRequest;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.OrderResponse;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.UpdateOrderRequest;

public class OrderAssembler {

    public static CreateOrderCommand toCommandFromRequest(CreateOrderRequest request){
        return new CreateOrderCommand(new UserAccountId(request.userAccountId()),request.amount(),request.membershipId());
    }

    public static UpdateOrderCommand toCommandFromRequest(Long orderId, UpdateOrderRequest request){
        return new UpdateOrderCommand(orderId, new UserAccountId(request.userAccountId()),request.amount(),request.membershipId());
    }

    public static OrderResponse toResponseFromEntity(Order order){
        return new OrderResponse(order.getId(),order.getUserAccountId().userAccountId(),order.getAmount(), order.getMembershipId());
    }

}
