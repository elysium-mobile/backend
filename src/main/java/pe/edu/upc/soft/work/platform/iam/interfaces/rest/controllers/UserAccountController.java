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
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.DeleteUserAccountCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetAllUserAccountQuery;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetUserAccountByIdQuery;
import pe.edu.upc.soft.work.platform.iam.domain.services.UserAccountCommandService;
import pe.edu.upc.soft.work.platform.iam.domain.services.UserAccountQueryService;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.assemblers.UserAccountAssembler;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/user_accounts", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "UsersAccount", description = "Endpoints for managing user accounts in the IAM system")
public class UserAccountController {

    private final UserAccountCommandService userAccountCommandService;
    private final UserAccountQueryService userAccountQueryService;

    public UserAccountController(UserAccountCommandService userAccountCommandService, UserAccountQueryService userAccountQueryService) {
        this.userAccountCommandService = userAccountCommandService;
        this.userAccountQueryService = userAccountQueryService;
    }

    @Operation(summary = "Create a new user account", description = "Create a new user account in the system",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Create user account request",
                    required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateUserRequest.class)
                    )
            ))

    @ApiResponses(value ={
            @ApiResponse(responseCode = "201", description = "User Account created successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "User Account not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<UserAccountResponse> createUserAccountt(@RequestBody CreateUserAccountRequest request){

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

    @Operation(summary = "Get all users accounts", description = "Retrieve a list of all users accounts in the system")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "No users found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<UserAccountResponse>> getAllUserAccounts(){
        var getAllUserAccountQuery = new GetAllUserAccountQuery();
        var usersAccount = this.userAccountQueryService.handle(getAllUserAccountQuery);

        var userAccountsResponses = usersAccount.stream().map(UserAccountAssembler::toResponseFromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(userAccountsResponses);
    }


    @Operation(summary = "Update user account information", description = "Update the information of an existing user")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserAccountResponse> updateUserAccount(@PathVariable Long id, @RequestBody UpdateUserAccountRequest request){
        var updateUserAccountCommand = UserAccountAssembler.toCommandFromRequest(id, request);
        var updated = this.userAccountCommandService.handle(updateUserAccountCommand);
        if (updated.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        var userAccountResponse = UserAccountAssembler.toResponseFromEntity(updated.get());
        return ResponseEntity.ok(userAccountResponse);
    }

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
