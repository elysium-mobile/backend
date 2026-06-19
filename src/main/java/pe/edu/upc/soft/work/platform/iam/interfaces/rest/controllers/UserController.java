package pe.edu.upc.soft.work.platform.iam.interfaces.rest.controllers;

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
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.DeleteUserCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetAllUsersQuery;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetUserByIdQuery;
import pe.edu.upc.soft.work.platform.iam.domain.services.UserCommandService;
import pe.edu.upc.soft.work.platform.iam.domain.services.UserQueryService;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.assemblers.UserAssembler;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.CreateUserRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.UpdateUserRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.UserResponse;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "Endpoints for managing users in the IAM system")
public class UserController {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public UserController(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    @Operation(summary = "Create a new user", description = "Create a new user in the system",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Create user request",
            required = true,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CreateUserRequest.class)
            )
    ))
    @ApiResponses(value ={
            @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request){
        var createUserCommand = UserAssembler.toCommandFromRequest(request);
        var userId = this.userCommandService.handle(createUserCommand);

        if (Objects.isNull(userId) || userId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        var getUserById = new GetUserByIdQuery(userId);
        var user = this.userQueryService.handle(getUserById);

        if (user.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        var userResponse = UserAssembler.toResponseFromEntity(user.get());
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all users", description = "Retrieve a list of all users in the system")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "No users found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        var getAllUsersQuery = new GetAllUsersQuery();
        var users = this.userQueryService.handle(getAllUsersQuery);

        var userResponses = users.stream()
                .map(UserAssembler::toResponseFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userResponses);
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a user by their unique identifier",
    parameters = @io.swagger.v3.oas.annotations.Parameter(name = "id", description = "The unique identifier of the user", required = true))
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "User retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/{id}")
   public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        var getUserByIdQuery = new GetUserByIdQuery(id);
        var user = userQueryService.handle(getUserByIdQuery);

        if (user.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        var userResponse = UserAssembler.toResponseFromEntity(user.get());
        return ResponseEntity.ok(userResponse);
    }


    @Operation(summary = "Update user information", description = "Update the information of an existing user",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "User data for update", required = true,
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = UpdateUserRequest.class))))
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request){
        var updateUserCommand= UserAssembler.toCommandFromRequest(id, request);
        var updatedUser = this.userCommandService.handle(updateUserCommand);
        if (updatedUser.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }
        var userResponse = UserAssembler.toResponseFromEntity(updatedUser.get());
        return ResponseEntity.ok(userResponse);
    }

    @Operation(summary = "Delete user by ID", description = "Delete a user by their unique identifier",
    parameters = @io.swagger.v3.oas.annotations.Parameter(name = "userId", description =
            "The unique identifier of the user to be deleted", required = true))
    @ApiResponses(value ={
            @ApiResponse(responseCode = "204", description = "User deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long userId){
        var deleteUserCommand = new DeleteUserCommand(userId);
        this.userCommandService.handle(deleteUserCommand);
        return ResponseEntity.noContent().build();
    }
}
