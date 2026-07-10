package pe.edu.upc.soft.work.platform.iam.interfaces.rest.controllers;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import pe.edu.upc.soft.work.platform.iam.domain.services.EmployeeProfileCommandService;
import pe.edu.upc.soft.work.platform.iam.domain.services.RRHHProfileCommandService;
import pe.edu.upc.soft.work.platform.iam.domain.services.UserAccountCommandService;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.assemblers.AuthenticationAssembler;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.assemblers.EmployeeProfileAssembler;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.assemblers.RRHHProfileAssembler;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.*;

/**
 * Controller for managing authentication and registration processes.
 * Provides endpoints for user sign-in and employee/RRHH registration.
 */
@RestController
@RequestMapping(value = "/api/v1/authentication", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Authentication Endpoints")
public class AuthenticationController {

    private final EmployeeProfileCommandService employeeProfileCommandService;
    private final RRHHProfileCommandService rrhhProfileCommandService;
    private final UserAccountCommandService userAccountCommandService;

    /**
     * Constructor for AuthenticationController.
     * Initializes the services for handling authentication and profile creation.
     * @param employeeProfileCommandService Service for handling employee profile operations
     * @param rrhhProfileCommandService     Service for handling RRHH profile operations
     * @param userAccountCommandService     Service for handling user account operations
     */
    public AuthenticationController(EmployeeProfileCommandService employeeProfileCommandService,
                                    RRHHProfileCommandService rrhhProfileCommandService,
                                    UserAccountCommandService userAccountCommandService) {
        this.employeeProfileCommandService = employeeProfileCommandService;
        this.rrhhProfileCommandService = rrhhProfileCommandService;
        this.userAccountCommandService = userAccountCommandService;
    }

    /**
     * Endpoint for user sign-in.
     * @param request Request object containing login credentials
     * @return ResponseEntity containing the authenticated user account and access token
     */
    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticatedUserAccountResponse> signIn(@Valid @RequestBody SignInRequest request) {
        var signInCommand = AuthenticationAssembler.toCommandFromRequestSignIn(request);
        var result = userAccountCommandService.handle(signInCommand);
        if (result.isEmpty()) return ResponseEntity.notFound().build();
        var response = AuthenticationAssembler.toResponseFromEntityUserAccount(
                result.get().getLeft(), result.get().getRight());
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint for the Google sign-in step.
     * Validates the provided Google id_token and, if a local account already exists for the
     * verified email, returns a registered response carrying the application access token.
     * When no account exists yet, it returns a registration-required response so the frontend
     * routes the user to the Google sign-up completion form. No account is created here.
     * @param request Request object containing the Google id_token
     * @return ResponseEntity containing the discriminated Google authentication response
     */
    @PostMapping("/google")
    public ResponseEntity<GoogleAuthenticationResponse> signInWithGoogle(@Valid @RequestBody GoogleSignInRequest request) {
        var googleSignInCommand = AuthenticationAssembler.toCommandFromRequestGoogleSignIn(request);
        var result = userAccountCommandService.handle(googleSignInCommand);
        return ResponseEntity.ok(AuthenticationAssembler.toGoogleAuthenticationResponse(result));
    }

    /**
     * Endpoint for completing an employee sign-up started with Google authentication.
     * Re-validates the Google id_token, creates the User, its Google-backed UserAccount and the
     * EmployeeProfile with the real form data, and returns the authenticated user account and token.
     * @param request Request object containing the Google id_token and employee registration details
     * @return ResponseEntity containing the authenticated user account and access token
     */
    @PostMapping("/sign-up/employee/google")
    public ResponseEntity<AuthenticatedUserAccountResponse> signUpEmployeeWithGoogle(@Valid @RequestBody GoogleEmployeeSignUpRequest request) {
        var command = AuthenticationAssembler.toCommandFromRequestGoogleSignUpEmployee(request);
        var result = employeeProfileCommandService.handle(command);
        if (result.isEmpty()) return ResponseEntity.badRequest().build();
        var response = AuthenticationAssembler.toResponseFromEntityUserAccount(
                result.get().getLeft(), result.get().getRight());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Endpoint for completing an RRHH sign-up started with Google authentication.
     * Re-validates the Google id_token, creates the User, its Google-backed UserAccount and the
     * RRHHProfile with the real form data, and returns the authenticated user account and token.
     * @param request Request object containing the Google id_token and RRHH registration details
     * @return ResponseEntity containing the authenticated user account and access token
     */
    @PostMapping("/sign-up/rrhh/google")
    public ResponseEntity<AuthenticatedUserAccountResponse> signUpRRHHWithGoogle(@Valid @RequestBody GoogleRRHHSignUpRequest request) {
        var command = AuthenticationAssembler.toCommandFromRequestGoogleSignUpRRHH(request);
        var result = rrhhProfileCommandService.handle(command);
        if (result.isEmpty()) return ResponseEntity.badRequest().build();
        var response = AuthenticationAssembler.toResponseFromEntityUserAccount(
                result.get().getLeft(), result.get().getRight());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Endpoint for employee sign-up.
     * @param request Request object containing employee registration details
     * @return ResponseEntity containing the created employee profile
     */
    @PostMapping("/sign-up/employee")
    public ResponseEntity<EmployeeProfileResponse> signUpEmployee(@Valid @RequestBody EmployeeProfileSignUpRequest request) {
        var signUpCommand = AuthenticationAssembler.toCommandFromRequestSignUpEmployeeProfile(request);
        var employeeProfile = employeeProfileCommandService.handle(signUpCommand);
        if (employeeProfile.isEmpty()) return ResponseEntity.badRequest().build();
        var response = EmployeeProfileAssembler.toResponseFromEntity(employeeProfile.get());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Endpoint for RRHH sign-up.
     * @param request Request object containing RRHH registration details
     * @return ResponseEntity containing the created RRHH profile
     */
    @PostMapping("/sign-up/rrhh")
    public ResponseEntity<RRHHProfileResponse> signUpRRHH(@Valid @RequestBody RRHHProfileSignUpRequest request) {
        var signUpCommand = AuthenticationAssembler.toCommandFromRequestSignUpRRHHProfile(request);
        var rrhhProfile = rrhhProfileCommandService.handle(signUpCommand);
        if (rrhhProfile.isEmpty()) return ResponseEntity.badRequest().build();
        var response = RRHHProfileAssembler.toResponseFromEntity(rrhhProfile.get());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
