package pe.edu.upc.soft.work.platform.iam.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.EmployeeSignUpCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.RRHHSignUpCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.SignInCommand;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.AuthenticatedUserAccountResponse;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.EmployeeProfileSignUpRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.RRHHProfileSignUpRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.SignInRequest;

/**
 * Assembler for converting authentication-related requests and entities.
 */
public class AuthenticationAssembler {

    /**
     * Converts a SignInRequest to a SignInCommand.
     * @param request the SignInRequest to be converted
     * @return a SignInCommand containing the credentials from the request
     */
    public static SignInCommand toCommandFromRequestSignIn(SignInRequest request){
        return new SignInCommand(
                request.email(),
                request.password()
        );
    }

    /**
     * Converts an EmployeeProfileSignUpRequest to an EmployeeSignUpCommand.
     * @param request the EmployeeProfileSignUpRequest to be converted
     * @return an EmployeeSignUpCommand containing the employee sign-up data from the request
     */
    public static EmployeeSignUpCommand toCommandFromRequestSignUpEmployeeProfile(EmployeeProfileSignUpRequest request){
        return new EmployeeSignUpCommand(
                request.name(),
                request.lastName(),
                request.phoneNumber(),
                request.dni(),
                request.email(),
                request.password(),
                request.anonymousName(),
                request.dateStart(),
                request.position(),
                request.salary()
        );
    }

    /**
     * Converts an RRHHProfileSignUpRequest to an RRHHSignUpCommand.
     * @param request the RRHHProfileSignUpRequest to be converted
     * @return an RRHHSignUpCommand containing the RRHH sign-up data from the request
     */
    public static RRHHSignUpCommand toCommandFromRequestSignUpRRHHProfile(RRHHProfileSignUpRequest request){
        return new RRHHSignUpCommand(
                request.name(),
                request.lastName(),
                request.phoneNumber(),
                request.dni(),
                request.email(),
                request.password(),
                request.anonymousName(),
                request.RRHHDepartment(),
                request.statusHierarchy()
        );
    }

    /**
     * Converts a UserAccount entity and a token to an AuthenticatedUserAccountResponse.
     * @param entity the UserAccount entity to be converted
     * @param token the authentication token
     * @return an AuthenticatedUserAccountResponse containing the user account data and the token
     */
    public static AuthenticatedUserAccountResponse toResponseFromEntityUserAccount(UserAccount entity, String token){
        return new AuthenticatedUserAccountResponse(entity.getId(),entity.getEmail(),token);
    }
}
