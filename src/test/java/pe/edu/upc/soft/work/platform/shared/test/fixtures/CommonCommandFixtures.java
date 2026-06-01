package pe.edu.upc.soft.work.platform.shared.test.fixtures;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

/**
 * Centralized, context-agnostic fixtures.
 *
 * <p>This class is the single source of truth for:
 * <ul>
 *   <li>Generic valid scalars (DNI, email, anonymous name, etc.) reused by every bounded context.</li>
 *   <li>{@link Stream} of {@link Arguments} suppliers for {@code @ParameterizedTest @MethodSource}
 *       declarations — invalid strings, malformed emails, boundary numeric values, etc.</li>
 * </ul>
 *
 * <p>Per-context fixture classes (e.g. {@code IamCommandFixtures},
 * {@code PaymentCommandFixtures}, {@code FeedbackCommandFixtures}) MUST
 * consume the constants and method sources defined here. They MUST NOT
 * redefine the same boundary values locally — duplication here is the
 * single most common source of drift in a test suite.
 *
 * <p>Adding a new method source: prefer a new {@code Stream<Arguments>}
 * provider in this class over expanding an existing one — readability
 * of {@code @MethodSource("invalidEmails")} beats clever overloads.
 */
public final class CommonCommandFixtures {

    private CommonCommandFixtures() {
        throw new AssertionError("CommonCommandFixtures is a utility class and must not be instantiated.");
    }

    // ---------- Valid scalar fixtures ----------

    public static final String VALID_DNI = "12345678";
    public static final String VALID_PHONE_NUMBER = "987654321";
    public static final String VALID_NAME = "Ada";
    public static final String VALID_LAST_NAME = "Lovelace";
    public static final String VALID_EMAIL = "ada@upc.edu.pe";
    public static final String VALID_PASSWORD = "Secret123!";
    public static final String VALID_ANONYMOUS_NAME = "ada_anon";

    public static final Long VALID_ID = 1L;
    public static final Long ANOTHER_VALID_ID = 2L;

    // ---------- Parameterized sources: invalid strings ----------

    /**
     * Stream of strings that must be rejected as "blank" by record
     * constructors and validators. Pair with a `@NullSource @EmptySource`
     * or expand here as needed.
     */
    public static Stream<Arguments> invalidBlankStrings() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(""),
                Arguments.of(" "),
                Arguments.of("   "),
                Arguments.of("\t"),
                Arguments.of("\n")
        );
    }

    /**
     * Stream of malformed e-mail strings. Use to feed
     * {@code @ParameterizedTest @MethodSource("malformedEmails")} when
     * validating email-shaped fields across contexts.
     */
    public static Stream<Arguments> malformedEmails() {
        return Stream.of(
                Arguments.of("plainaddress"),
                Arguments.of("@no-local-part.com"),
                Arguments.of("no-at-sign.com"),
                Arguments.of("missing-tld@example"),
                Arguments.of("two@@signs.com"),
                Arguments.of("spaces in@email.com"),
                Arguments.of("trailing@dot.com.")
        );
    }

    /**
     * Stream of DNIs that violate the 8-character length rule enforced
     * by {@code pe.edu.upc.soft.work.platform.shared.utils.Util#DNI_LENGTH}.
     */
    public static Stream<Arguments> invalidDniLengths() {
        return Stream.of(
                Arguments.of(""),
                Arguments.of("1"),
                Arguments.of("1234567"),    // 7 chars
                Arguments.of("123456789"),  // 9 chars
                Arguments.of("123456789012") // way too long
        );
    }

    // ---------- Parameterized sources: numeric boundaries ----------

    /**
     * Stream of identifier boundary values: zero, negative, and
     * {@link Long#MIN_VALUE}. Use against records that enforce
     * {@code id > 0} (e.g. {@code GetUserByIdQuery}).
     */
    public static Stream<Arguments> nonPositiveIds() {
        return Stream.of(
                Arguments.of(0L),
                Arguments.of(-1L),
                Arguments.of(Long.MIN_VALUE)
        );
    }

    /**
     * Stream of identifier values that are strictly positive — used as
     * happy-path inputs for "by id" queries.
     */
    public static Stream<Arguments> validPositiveIds() {
        return Stream.of(
                Arguments.of(1L),
                Arguments.of(42L),
                Arguments.of(Long.MAX_VALUE)
        );
    }

    /**
     * Stream of integer salary / amount boundaries: useful for
     * services that operate on {@code Integer} salaries or fees.
     */
    public static Stream<Arguments> integerAmountBoundaries() {
        return Stream.of(
                Arguments.of(0),
                Arguments.of(1),
                Arguments.of(Integer.MAX_VALUE)
        );
    }
}
