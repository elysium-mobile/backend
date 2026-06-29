package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.DeleteOrderCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetAllOrderQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetOrderByIdQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetOrderByUserAccountIdQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.OrderCommandService;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.OrderQueryService;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.assemblers.OrderAssembler;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.CreateOrderRequest;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.MembershipResponse;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.OrderResponse;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.UpdateOrderRequest;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Controller for managing Orders in the system.
 * Provides endpoints for creating, retrieving, updating, and deleting Orders.
 */
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Orders", description = "Endpoints for managing Orders")
public class OrderController {

    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;

    /**
     * Constructor for OrderController.
     * Initializes the command and query services for handling Order operations.
     * @param orderCommandService Service for handling commands related to Orders
     * @param orderQueryService   Service for handling queries related to Orders
     */
    public OrderController(OrderCommandService orderCommandService, OrderQueryService orderQueryService){
        this.orderCommandService = orderCommandService;
        this.orderQueryService = orderQueryService;
    }

    /**
     * Endpoint for creating a new Order.
     * @param request Request object containing the details of the Order to be created
     * @return ResponseEntity containing the created OrderResponse and the appropriate HTTP status code
     */
    @Operation(summary = "Create a new Order", description = "Create a new Order in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request){
        var createOrderCommand = OrderAssembler.toCommandFromRequest(request);
        var orderId = this.orderCommandService.handle(createOrderCommand);

        if (Objects.isNull(orderId))
        {
            return ResponseEntity.badRequest().build();
        }

        var getOrderById = new GetOrderByIdQuery(orderId);
        var order = this.orderQueryService.handle(getOrderById);

        if (order.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        var orderResponse = OrderAssembler.toResponseFromEntity(order.get());
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    /**
     * Endpoint for retrieving all Orders.
     * @return ResponseEntity containing a list of OrderResponse objects
     */
    @Operation(summary = "Get all Orders", description = "Retrieve a list of all Orders in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "404", description = "No Order found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(){
        var getALlOrderQuery = new GetAllOrderQuery();
        var orders = this.orderQueryService.handle(getALlOrderQuery);

        var orderResponses = orders.stream()
                .map(OrderAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderResponses);
    }

    /**
     * Endpoint for retrieving a specific Order by ID.
     * @param id ID of the Order to be retrieved
     * @return ResponseEntity containing the OrderResponse if found
     */
    @Operation(summary = "Get Order by ID", description = "Retrieve a Order by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id){
        var getOrderByIdQuery= new GetOrderByIdQuery(id);
        var order = orderQueryService.handle(getOrderByIdQuery);

        if (order.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        var orderResponse = OrderAssembler.toResponseFromEntity(order.get());
        return ResponseEntity.ok(orderResponse);
    }

    /**
     * Endpoint for updating an existing Order by ID.
     * @param id ID of the Order to be updated
     * @param request Request object containing the updated details
     * @return ResponseEntity containing the updated OrderResponse if successful
     */
    @Operation(summary = "Update Order information", description = "Update the information of an existing Order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable Long id, @RequestBody UpdateOrderRequest request){
        var updateOrderCommand = OrderAssembler.toCommandFromRequest(id,request);
        var updateOrder = this.orderCommandService.handle(updateOrderCommand);
        if (updateOrder.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }
        var orderResponse = OrderAssembler.toResponseFromEntity(updateOrder.get());
        return ResponseEntity.ok(orderResponse);
    }

    /**
     * Endpoint for deleting an Order by ID.
     * @param id ID of the Order to be deleted
     * @return ResponseEntity with no content if deleted successfully
     */
    @Operation(summary = "Delete Order by ID", description = "Delete a Order by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderById(@PathVariable Long id){
        var deleteOrderCommand = new DeleteOrderCommand(id);
        this.orderCommandService.handle(deleteOrderCommand);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint for retrieving Orders associated with a specific User Account.
     * @param userAccountId ID of the User Account
     * @return ResponseEntity containing a list of OrderResponse objects
     */
    @Operation(summary = "Get Orders by User Account ID", description = "Retrieve a list of Orders associated with a specific User Account ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "404", description = "No Orders found for the given User Account ID", content = @Content)
    })
    @GetMapping("/userAccount/{userAccountId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserAccountId(@PathVariable Long userAccountId){
        var getOrderByUserAccountIdQuery = new GetOrderByUserAccountIdQuery(userAccountId);
        var orders = this.orderQueryService.handle(getOrderByUserAccountIdQuery);

        if (orders.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        var orderResponses = orders.stream()
                .map(OrderAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderResponses);
    }
}
