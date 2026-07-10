package pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.DeletePaymentCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetPaymentByIdQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.queries.GetAllPaymentQuery;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.PaymentCommandService;
import pe.edu.upc.soft.work.platform.payment.service.domain.services.PaymentQueryService;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.assemblers.PaymentAssembler;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.CreatePaymentRequest;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.UpdatePaymentRequest;
import pe.edu.upc.soft.work.platform.payment.service.interfaces.rest.resources.PaymentResponse;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Controller for managing Payments in the system.
 * Provides endpoints for creating, retrieving, updating, and deleting Payments.
 */
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/payments", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Payments", description = "Endpoints for managing Payments")
public class PaymentController {

    private final PaymentCommandService paymentCommandService;
    private final PaymentQueryService paymentQueryService;

    /**
     * Constructor for PaymentController.
     * Initializes the command and query services for handling Payment operations.
     * @param paymentCommandService Service for handling commands related to Payments
     * @param paymentQueryService   Service for handling queries related to Payments
     */
    public PaymentController(PaymentCommandService paymentCommandService, PaymentQueryService paymentQueryService) {
        this.paymentCommandService = paymentCommandService;
        this.paymentQueryService = paymentQueryService;
    }

    /**
     * Endpoint for creating a new Payment.
     * @param request Request object containing the details of the Payment to be created
     * @return ResponseEntity containing the created PaymentResponse and the appropriate HTTP status code
     */
    @Operation(summary = "Create a new Payment", description = "Create a new Payment in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment created successfully", 
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                            schema = @Schema(implementation = PaymentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Payment not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody CreatePaymentRequest request) {
        var createPaymentCommand = PaymentAssembler.toCommandFromRequest(request);
        var paymentId = this.paymentCommandService.handle(createPaymentCommand);

        if (Objects.isNull(paymentId) || paymentId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        var getPaymentById = new GetPaymentByIdQuery(paymentId);
        var payment = this.paymentQueryService.handle(getPaymentById);

        if (payment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var paymentResponse = PaymentAssembler.toResponseFromEntity(payment.get());
        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }

    /**
     * Endpoint for retrieving all Payments.
     * @return ResponseEntity containing a list of PaymentResponse objects
     */
    @Operation(summary = "Get all Payments", description = "Retrieve a list of all Payments in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payments retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PaymentResponse.class))),
            @ApiResponse(responseCode = "404", description = "No Payments found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        var getAllPaymentQuery = new GetAllPaymentQuery();
        var payments = this.paymentQueryService.handle(getAllPaymentQuery);

        var paymentResponses = payments.stream()
                .map(PaymentAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(paymentResponses);
    }

    /**
     * Endpoint for retrieving a specific Payment by ID.
     * @param id ID of the Payment to be retrieved
     * @return ResponseEntity containing the PaymentResponse if found
     */
    @Operation(summary = "Get Payment by ID", description = "Retrieve a Payment by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PaymentResponse.class))),
            @ApiResponse(responseCode = "404", description = "Payment not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id) {
        var getPaymentByIdQuery = new GetPaymentByIdQuery(id);
        var payment = paymentQueryService.handle(getPaymentByIdQuery);

        if (payment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var paymentResponse = PaymentAssembler.toResponseFromEntity(payment.get());
        return ResponseEntity.ok(paymentResponse);
    }

    /**
     * Endpoint for updating an existing Payment by ID.
     * @param id ID of the Payment to be updated
     * @param request Request object containing the updated details
     * @return ResponseEntity containing the updated PaymentResponse if successful
     */
    @Operation(summary = "Update Payment information", description = "Update the information of an existing Payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PaymentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Payment not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponse> updatePayment(@PathVariable Long id, @Valid @RequestBody UpdatePaymentRequest request) {
        var updatePaymentCommand = PaymentAssembler.toCommandFromRequest(id, request);
        var updatedPayment = this.paymentCommandService.handle(updatePaymentCommand);
        if (updatedPayment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var paymentResponse = PaymentAssembler.toResponseFromEntity(updatedPayment.get());
        return ResponseEntity.ok(paymentResponse);
    }

    /**
     * Endpoint for deleting a Payment by ID.
     * @param id ID of the Payment to be deleted
     * @return ResponseEntity with no content if deleted successfully
     */
    @Operation(summary = "Delete Payment by ID", description = "Delete a Payment by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Payment deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Payment not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePaymentById(@PathVariable Long id) {
        var deletePaymentCommand = new DeletePaymentCommand(id);
        this.paymentCommandService.handle(deletePaymentCommand);
        return ResponseEntity.noContent().build();
    }
}
