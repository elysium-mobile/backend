package pe.edu.upc.soft.work.platform.iam.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.EmployeeSignUpCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.RRHHSignUpCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.SignInCommand;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.AuthenticatedUserAccountResponse;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.EmployeeProfileSignUpRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.RRHHProfileSignUpRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.SignInRequest;

public class AuthenticationAssembler {


    public static SignInCommand toCommandFromRequestSignIn(SignInRequest request){
        return new SignInCommand(
                request.email(),
                request.password()
        );
    }

    public static EmployeeSignUpCommand toCommandFromRequestSignUpEmployeeProfile(EmployeeProfileSignUpRequest request){
        return new EmployeeSignUpCommand(
                request.name(),
                request.lastname(),
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

    public static RRHHSignUpCommand toCommandFromRequestSignUpRRHHProfile(RRHHProfileSignUpRequest request){
        return new RRHHSignUpCommand(
                request.name(),
                request.lastname(),
                request.phoneNumber(),
                request.dni(),
                request.email(),
                request.password(),
                request.RRHHDepartment(),
                request.statusHierarchy()
        );
    }

    public static AuthenticatedUserAccountResponse toResponseFromEntityUserAccount(UserAccount entity, String token){
        return new AuthenticatedUserAccountResponse(entity.getId(),entity.getEmail(),token);
    }
}
