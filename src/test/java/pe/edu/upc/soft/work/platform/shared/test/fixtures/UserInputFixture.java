package pe.edu.upc.soft.work.platform.shared.test.fixtures;

import lombok.Builder;
import lombok.Value;

/**
 * Cross-context, immutable, Lombok-{@link Builder}-backed fixture model
 * for "person-like" inputs (name, last name, dni, phone number, email,
 * password, anonymous name).
 *
 * <p><strong>Why a single shared input model:</strong> several bounded
 * contexts (IAM, Feedback, Worker Forum, etc.) accept user-shaped
 * fields. Each context's per-feature fixture class should accept (or
 * provide a default of) this object and project it into its own
 * command/request, so that any change to canonical "valid" person data
 * happens here exactly once.
 *
 * <p><strong>Usage in a test:</strong>
 * <pre>{@code
 * UserInputFixture input = UserInputFixture.valid().toBuilder()
 *         .dni("99999999")
 *         .build();
 * CreateUserCommand command = IamCommandFixtures.createUserCommandFrom(input);
 * }</pre>
 *
 * <p>The {@code toBuilder = true} on {@link Builder} allows tests to
 * mutate one or two fields without restating the entire object — the
 * key benefit of this pattern over plain factory methods.
 */
@Value
@Builder(toBuilder = true)
public class UserInputFixture {

    String name;
    String lastName;
    String phoneNumber;
    String dni;
    String email;
    String password;
    String anonymousName;

    /**
     * Canonical valid input. Mutate via {@code .toBuilder().field(...).build()}.
     *
     * @return a UserInputFixture populated with the canonical valid values
     *         from {@link CommonCommandFixtures}
     */
    public static UserInputFixture valid() {
        return UserInputFixture.builder()
                .name(CommonCommandFixtures.VALID_NAME)
                .lastName(CommonCommandFixtures.VALID_LAST_NAME)
                .phoneNumber(CommonCommandFixtures.VALID_PHONE_NUMBER)
                .dni(CommonCommandFixtures.VALID_DNI)
                .email(CommonCommandFixtures.VALID_EMAIL)
                .password(CommonCommandFixtures.VALID_PASSWORD)
                .anonymousName(CommonCommandFixtures.VALID_ANONYMOUS_NAME)
                .build();
    }
}
