package pe.edu.upc.soft.work.platform.iam.interfaces.rest.assemblers;

import org.apache.commons.lang3.tuple.ImmutablePair;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.EmployeeSignUpCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.GoogleEmployeeSignUpCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.GoogleRRHHSignUpCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.GoogleSignInCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.RRHHSignUpCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.SignInCommand;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.AuthenticatedUserAccountResponse;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.EmployeeProfileSignUpRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.GoogleAuthenticationResponse;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.GoogleEmployeeSignUpRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.GoogleRRHHSignUpRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.GoogleSignInRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.RRHHProfileSignUpRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.SignInRequest;

import java.util.Optional;

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
     * Converts a GoogleSignInRequest to a GoogleSignInCommand.
     * @param request the GoogleSignInRequest to be converted
     * @return a GoogleSignInCommand containing the Google id_token from the request
     */
    public static GoogleSignInCommand toCommandFromRequestGoogleSignIn(GoogleSignInRequest request){
        return new GoogleSignInCommand(
                request.idToken()
        );
    }

    /**
     * Converts a GoogleEmployeeSignUpRequest to a GoogleEmployeeSignUpCommand.
     * @param request the GoogleEmployeeSignUpRequest to be converted
     * @return a GoogleEmployeeSignUpCommand containing the Google id_token and employee data
     */
    public static GoogleEmployeeSignUpCommand toCommandFromRequestGoogleSignUpEmployee(GoogleEmployeeSignUpRequest request){
        return new GoogleEmployeeSignUpCommand(
                request.idToken(),
                request.name(),
                request.lastName(),
                request.phoneNumber(),
                request.dni(),
                request.dateStart(),
                request.position(),
                request.salary()
        );
    }

    /**
     * Converts a GoogleRRHHSignUpRequest to a GoogleRRHHSignUpCommand.
     * @param request the GoogleRRHHSignUpRequest to be converted
     * @return a GoogleRRHHSignUpCommand containing the Google id_token and RRHH data
     */
    public static GoogleRRHHSignUpCommand toCommandFromRequestGoogleSignUpRRHH(GoogleRRHHSignUpRequest request){
        return new GoogleRRHHSignUpCommand(
                request.idToken(),
                request.name(),
                request.lastName(),
                request.phoneNumber(),
                request.dni(),
                request.RRHHDepartment(),
                request.statusHierarchy()
        );
    }

    /**
     * Converts the result of the Google sign-in step to a GoogleAuthenticationResponse.
     * When the account already exists the pair carries the UserAccount and its access token and a
     * registered response is produced; otherwise a registration-required response is returned.
     * @param result the Optional pair of authenticated UserAccount and access token
     * @return a GoogleAuthenticationResponse discriminating registered vs registration-required
     */
    public static GoogleAuthenticationResponse toGoogleAuthenticationResponse(
            Optional<ImmutablePair<UserAccount, String>> result){
        return result
                .map(pair -> GoogleAuthenticationResponse.registered(
                        pair.getLeft().getId(), pair.getLeft().getEmail(), pair.getRight()))
                .orElseGet(GoogleAuthenticationResponse::registrationRequired);
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
