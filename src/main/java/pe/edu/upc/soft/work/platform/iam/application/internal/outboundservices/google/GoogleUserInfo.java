package pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.google;

/**
 * Immutable representation of the verified claims extracted from a Google id_token.
 * This payload is produced by the {@link GoogleTokenService} once the token has been
 * validated, and is consumed by the application layer to load or create a local user.
 *
 * @param subject the Google unique subject identifier (the {@code sub} claim)
 * @param email the verified email address associated with the Google account
 * @param givenName the given (first) name provided by Google, may be {@code null}
 * @param familyName the family (last) name provided by Google, may be {@code null}
 */
public record GoogleUserInfo(String subject, String email, String givenName, String familyName) {
}
