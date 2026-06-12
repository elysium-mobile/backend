package pe.edu.upc.soft.work.platform.iam.interfaces.rest.controllers;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.soft.work.platform.iam.domain.services.EmployeeProfileCommandService;
import pe.edu.upc.soft.work.platform.iam.domain.services.RRHHProfileCommandService;
import pe.edu.upc.soft.work.platform.iam.domain.services.UserAccountCommandService;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.assemblers.AuthenticationAssembler;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.assemblers.EmployeeProfileAssembler;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.assemblers.RRHHProfileAssembler;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.*;

@RestController
@RequestMapping(value = "/api/v1/authentication", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Authentication Endpoints")
public class AuthenticationController {

    private final EmployeeProfileCommandService employeeProfileCommandService;
    private final RRHHProfileCommandService rrhhProfileCommandService;
    private final UserAccountCommandService userAccountCommandService;

    public AuthenticationController(EmployeeProfileCommandService employeeProfileCommandService,
                                    RRHHProfileCommandService rrhhProfileCommandService,
                                    UserAccountCommandService userAccountCommandService) {
        this.employeeProfileCommandService = employeeProfileCommandService;
        this.rrhhProfileCommandService = rrhhProfileCommandService;
        this.userAccountCommandService = userAccountCommandService;
    }

//    @PostMapping("/sign-in/employee")
//    public ResponseEntity<AuthenticatedUserAccountResponse> signInEmployee(@RequestBody SignInRequest request) {
//        var signInCommand = AuthenticationAssembler.toCommandFromRequestSignIn(request);
//        var authenticatedUserAccount = employeeProfileCommandService.handle(signInCommand);
//        if (authenticatedUserAccount.isEmpty()) return ResponseEntity.notFound().build();
//        var response = AuthenticationAssembler.toResponseFromEntityUserAccount(
//                authenticatedUserAccount.get().getLeft(), authenticatedUserAccount.get().getRight());
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/sign-in/rrhh")
//    public ResponseEntity<AuthenticatedUserAccountResponse> signInRRHH(@RequestBody SignInRequest request) {
//        var signInCommand = AuthenticationAssembler.toCommandFromRequestSignIn(request);
//        var authenticatedUserAccount = rrhhProfileCommandService.handle(signInCommand);
//        if (authenticatedUserAccount.isEmpty()) return ResponseEntity.notFound().build();
//        var response = AuthenticationAssembler.toResponseFromEntityUserAccount(
//                authenticatedUserAccount.get().getLeft(), authenticatedUserAccount.get().getRight());
//        return ResponseEntity.ok(response);
//    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticatedUserAccountResponse> signIn(@RequestBody SignInRequest request) {
        var signInCommand = AuthenticationAssembler.toCommandFromRequestSignIn(request);
        var result = userAccountCommandService.handle(signInCommand);
        if (result.isEmpty()) return ResponseEntity.notFound().build();
        var response = AuthenticationAssembler.toResponseFromEntityUserAccount(
                result.get().getLeft(), result.get().getRight());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sign-up/employee")
    public ResponseEntity<EmployeeProfileResponse> signUpEmployee(@RequestBody EmployeeProfileSignUpRequest request) {
        var signUpCommand = AuthenticationAssembler.toCommandFromRequestSignUpEmployeeProfile(request);
        var employeeProfile = employeeProfileCommandService.handle(signUpCommand);
        if (employeeProfile.isEmpty()) return ResponseEntity.badRequest().build();
        var response = EmployeeProfileAssembler.toResponseFromEntity(employeeProfile.get());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/sign-up/rrhh")
    public ResponseEntity<RRHHProfileResponse> signUpRRHH(@RequestBody RRHHProfileSignUpRequest request) {
        var signUpCommand = AuthenticationAssembler.toCommandFromRequestSignUpRRHHProfile(request);
        var rrhhProfile = rrhhProfileCommandService.handle(signUpCommand);
        if (rrhhProfile.isEmpty()) return ResponseEntity.badRequest().build();
        var response = RRHHProfileAssembler.toResponseFromEntity(rrhhProfile.get());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
