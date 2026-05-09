package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.controllers;

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
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteMessageCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetMessageByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAllMessageQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.MessageCommandService;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.MessageQueryService;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.assemblers.MessageAssembler;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.CreateMessageRequest;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.UpdateMessageRequest;
import pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources.MessageResponse;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/messages", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Messages", description = "Endpoints for managing Messages")
public class MessageController {

    private final MessageCommandService messageCommandService;
    private final MessageQueryService messageQueryService;

    public MessageController(MessageCommandService messageCommandService, MessageQueryService messageQueryService) {
        this.messageCommandService = messageCommandService;
        this.messageQueryService = messageQueryService;
    }

    @Operation(summary = "Create a new Message", description = "Create a new Message in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Message created successfully", 
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                            schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Message not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<MessageResponse> createMessage(@RequestBody CreateMessageRequest request) {
        var createMessageCommand = MessageAssembler.toCommandFromRequest(request);
        var messageId = this.messageCommandService.handle(createMessageCommand);

        if (Objects.isNull(messageId) || messageId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        var getMessageById = new GetMessageByIdQuery(messageId);
        var message = this.messageQueryService.handle(getMessageById);

        if (message.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var messageResponse = MessageAssembler.toResponseFromEntity(message.get());
        return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all Messages", description = "Retrieve a list of all Messages in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messages retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "404", description = "No Messages found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<MessageResponse>> getAllMessages() {
        var getAllMessageQuery = new GetAllMessageQuery();
        var messages = this.messageQueryService.handle(getAllMessageQuery);

        var messageResponses = messages.stream()
                .map(MessageAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messageResponses);
    }

    @Operation(summary = "Get Message by ID", description = "Retrieve a Message by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "404", description = "Message not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> getMessageById(@PathVariable Long id) {
        var getMessageByIdQuery = new GetMessageByIdQuery(id);
        var message = messageQueryService.handle(getMessageByIdQuery);

        if (message.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var messageResponse = MessageAssembler.toResponseFromEntity(message.get());
        return ResponseEntity.ok(messageResponse);
    }

    @Operation(summary = "Update Message information", description = "Update the information of an existing Message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Message not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateMessage(@PathVariable Long id, @RequestBody UpdateMessageRequest request) {
        var updateMessageCommand = MessageAssembler.toCommandFromRequest(id, request);
        var updatedMessage = this.messageCommandService.handle(updateMessageCommand);
        if (updatedMessage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var messageResponse = MessageAssembler.toResponseFromEntity(updatedMessage.get());
        return ResponseEntity.ok(messageResponse);
    }

    @Operation(summary = "Delete Message by ID", description = "Delete a Message by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Message deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Message not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessageById(@PathVariable Long id) {
        var deleteMessageCommand = new DeleteMessageCommand(id);
        this.messageCommandService.handle(deleteMessageCommand);
        return ResponseEntity.noContent().build();
    }
}
