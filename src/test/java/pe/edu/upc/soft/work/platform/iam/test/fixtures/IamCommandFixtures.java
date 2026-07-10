package pe.edu.upc.soft.work.platform.iam.test.fixtures;

import pe.edu.upc.soft.work.platform.iam.domain.model.commands.CreateEmployeeProfileCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.CreateRRHHProfileCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.CreateUserAccountCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.CreateUserCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.EmployeeSignUpCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.GoogleSignInCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.RRHHSignUpCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.SignInCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.UpdateUserCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.valueobjects.CompanyId;
import pe.edu.upc.soft.work.platform.iam.domain.model.valueobjects.MembershipId;
import pe.edu.upc.soft.work.platform.iam.domain.model.valueobjects.WorkOfTeamId;
import pe.edu.upc.soft.work.platform.shared.test.fixtures.UserInputFixture;

import java.util.Date;

/**
 * IAM-specific command factories. ALL person-shaped data is sourced
 * from {@link UserInputFixture} so that "what is a valid user?" is
 * defined exactly once across the project.
 *
 * <p>This class is the architectural template every other bounded
 * context fixture must follow:
 * <ol>
 *   <li>Live under {@code <context>.test.fixtures} in test scope.</li>
 *   <li>Accept (or default to) shared input models like
 *       {@link UserInputFixture} via Lombok builder, never duplicating
 *       valid-data constants.</li>
 *   <li>Expose static {@code …From(input)} factories that produce
 *       commands / queries / aggregates with no hidden state.</li>
 *   <li>Never expose mutable shared state — every call returns a fresh
 *       instance.</li>
 * </ol>
 */
public final class IamCommandFixtures {

    private IamCommandFixtures() {
        throw new AssertionError("IamCommandFixtures is a utility class and must not be instantiated.");
    }

    public static CreateUserCommand createUserCommandFrom(UserInputFixture input) {
        return new CreateUserCommand(
                input.getName(),
                input.getLastName(),
                input.getPhoneNumber(),
                input.getDni());
    }

    public static CreateUserCommand validCreateUserCommand() {
        return createUserCommandFrom(UserInputFixture.valid());
    }

    public static UpdateUserCommand updateUserCommandFrom(Long userId, UserInputFixture input) {
        return new UpdateUserCommand(
                userId,
                input.getName(),
                input.getLastName(),
                input.getPhoneNumber(),
                input.getDni());
    }

    public static CreateUserAccountCommand createUserAccountCommandFrom(
            Long userId, UserInputFixture input) {
        return new CreateUserAccountCommand(
                userId,
                input.getEmail(),
                input.getPassword(),
                input.getAnonymousName(),
                new MembershipId(0L),
                new CompanyId(0L));
    }

    public static SignInCommand signInCommandFrom(UserInputFixture input) {
        return new SignInCommand(input.getEmail(), input.getPassword());
    }

    public static final String VALID_GOOGLE_ID_TOKEN = "valid.google.id-token";

    public static GoogleSignInCommand googleSignInCommand() {
        return new GoogleSignInCommand(VALID_GOOGLE_ID_TOKEN);
    }

    public static CreateEmployeeProfileCommand validCreateEmployeeProfileCommand(
            Long userAccountId, Long workOfTeamId) {
        return new CreateEmployeeProfileCommand(
                new Date(0L), "Engineer", 5000, userAccountId, new WorkOfTeamId(workOfTeamId));
    }

    public static EmployeeSignUpCommand employeeSignUpCommandFrom(UserInputFixture input) {
        return new EmployeeSignUpCommand(
                input.getName(),
                input.getLastName(),
                input.getPhoneNumber(),
                input.getDni(),
                input.getEmail(),
                input.getPassword(),
                input.getAnonymousName(),
                new Date(0L),
                "Engineer",
                5000);
    }

    public static CreateRRHHProfileCommand validCreateRRHHProfileCommand(Long userAccountId) {
        return new CreateRRHHProfileCommand("Recruiting", "Senior", userAccountId);
    }

    public static RRHHSignUpCommand rrhhSignUpCommandFrom(UserInputFixture input) {
        return new RRHHSignUpCommand(
                input.getName(),
                input.getLastName(),
                input.getPhoneNumber(),
                input.getDni(),
                input.getEmail(),
                input.getPassword(),
                input.getAnonymousName(),
                "Recruiting",
                "Senior");
    }
}
