package pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.google;

/**
 * Outbound service (port) for validating Google id_tokens.
 * Implementations are responsible for verifying the token integrity, audience and
 * expiration against Google, returning the verified account claims when the token is valid.
 */
public interface GoogleTokenService {

    /**
     * Validate a Google id_token and extract its verified claims.
     *
     * @param idToken the Google id_token to be validated
     * @return a {@link GoogleUserInfo} containing the verified claims of the Google account
     * @throws IllegalArgumentException if the token is invalid, expired, has an unexpected
     *         audience or the email is not verified
     */
    GoogleUserInfo verify(String idToken);
}
