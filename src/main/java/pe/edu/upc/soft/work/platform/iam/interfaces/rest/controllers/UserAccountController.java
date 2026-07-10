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
import jakarta.validation.Valid;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.DeleteUserAccountCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetAllUserAccountQuery;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetUserAccountByIdQuery;
import pe.edu.upc.soft.work.platform.iam.domain.services.UserAccountCommandService;
import pe.edu.upc.soft.work.platform.iam.domain.services.UserAccountQueryService;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.assemblers.UserAccountAssembler;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.CreateUserAccountRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.UpdateUserAccountRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.UserAccountResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing user accounts in the IAM system.
 * Provides endpoints for creating, retrieving, updating, and deleting user accounts.
 */
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/user_accounts", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "User Accounts", description = "Endpoints for managing user accounts in the IAM system")
public class UserAccountController {

    private final UserAccountCommandService userAccountCommandService;
    private final UserAccountQueryService userAccountQueryService;

    /**
     * Constructor for UserAccountController.
     * Initializes the command and query services for handling user account operations.
     * @param userAccountCommandService Service for handling commands related to user accounts
     * @param userAccountQueryService   Service for handling queries related to user accounts
     */
    public UserAccountController(UserAccountCommandService userAccountCommandService, UserAccountQueryService userAccountQueryService) {
        this.userAccountCommandService = userAccountCommandService;
        this.userAccountQueryService = userAccountQueryService;
    }

    /**
     * Endpoint for creating a new user account.
     * @param request Request object containing the details of the user account to be created
     * @return ResponseEntity containing the created user account and the appropriate HTTP status code
     */
    @Operation(summary = "Create a new user account", description = "Create a new user account in the system",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Create user account request",
                    required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateUserAccountRequest.class)
                    )
            ))

    @ApiResponses(value ={
            @ApiResponse(responseCode = "201", description = "User Account created successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserAccountResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "User Account not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<UserAccountResponse> createUserAccount(@Valid @RequestBody CreateUserAccountRequest request){

        var createUserAccountCommand = UserAccountAssembler.toCommandFromRequest(request);
        var userAccountId = this.userAccountCommandService.handle( createUserAccountCommand );

        if(userAccountId == null){
            return ResponseEntity.badRequest().build();
        }

        var getUserAccountById = new GetUserAccountByIdQuery(userAccountId);
        var userAccount = this.userAccountQueryService.handle(getUserAccountById);

        if(userAccount.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        var userAccountResponse = UserAccountAssembler.toResponseFromEntity(userAccount.get());
        return new ResponseEntity<>(userAccountResponse, HttpStatus.CREATED);
    }

    /**
     * Endpoint for retrieving all user accounts.
     * @return ResponseEntity containing a list of UserAccountResponse objects
     */
    @Operation(summary = "Get all users accounts", description = "Retrieve a list of all users accounts in the system")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserAccountResponse.class))),
            @ApiResponse(responseCode = "404", description = "No users found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<UserAccountResponse>> getAllUserAccounts(){
        var getAllUserAccountQuery = new GetAllUserAccountQuery();
        var usersAccount = this.userAccountQueryService.handle(getAllUserAccountQuery);

        var userAccountsResponses = usersAccount.stream().map(UserAccountAssembler::toResponseFromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(userAccountsResponses);
    }


    /**
     * Endpoint for updating an existing user account.
     * @param id The unique identifier of the user account
     * @param request Request object containing the updated details
     * @return ResponseEntity containing the updated UserAccountResponse if successful
     */
    @Operation(summary = "Update user account information", description = "Update the information of an existing user")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserAccountResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserAccountResponse> updateUserAccount(@PathVariable Long id, @Valid @RequestBody UpdateUserAccountRequest request){
        var updateUserAccountCommand = UserAccountAssembler.toCommandFromRequest(id, request);
        var updated = this.userAccountCommandService.handle(updateUserAccountCommand);
        if (updated.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        var userAccountResponse = UserAccountAssembler.toResponseFromEntity(updated.get());
        return ResponseEntity.ok(userAccountResponse);
    }

    /**
     * Endpoint for deleting a user account by ID.
     * @param id The unique identifier of the user account to be deleted
     * @return ResponseEntity indicating the outcome of the deletion
     */
    @Operation(summary = "Delete user account by ID", description = "Delete a user account by their unique identifier",
            parameters = @io.swagger.v3.oas.annotations.Parameter(name = "userId", description =
                    "The unique identifier of the user account to be deleted", required = true))
    @ApiResponses(value ={
            @ApiResponse(responseCode = "204", description = "User deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserAccountById(@PathVariable Long id){
        var deleteUserAccountCommand = new DeleteUserAccountCommand(id);
        this.userAccountCommandService.handle( deleteUserAccountCommand );
        return ResponseEntity.ok().build();
    }
}
