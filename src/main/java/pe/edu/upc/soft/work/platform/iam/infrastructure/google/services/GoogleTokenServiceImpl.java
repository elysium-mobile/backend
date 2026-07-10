package pe.edu.upc.soft.work.platform.iam.infrastructure.google.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.google.GoogleTokenService;
import pe.edu.upc.soft.work.platform.iam.application.internal.outboundservices.google.GoogleUserInfo;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

/**
 * Infrastructure adapter that validates Google id_tokens using the official
 * {@link GoogleIdTokenVerifier} from the Google API client library.
 *
 * <p>The verifier checks the token signature against Google's public certificates, its
 * expiration and that the audience matches the configured OAuth client id. When the token
 * is valid the verified claims are exposed as a {@link GoogleUserInfo}; otherwise an
 * {@link IllegalArgumentException} is raised so the application layer can reject the sign-in.</p>
 */
@Service
public class GoogleTokenServiceImpl implements GoogleTokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleTokenServiceImpl.class);

    private static final String GIVEN_NAME_CLAIM = "given_name";
    private static final String FAMILY_NAME_CLAIM = "family_name";

    private final GoogleIdTokenVerifier verifier;

    /**
     * Constructor for GoogleTokenServiceImpl.
     * Builds a {@link GoogleIdTokenVerifier} restricted to the configured OAuth client id,
     * so that only tokens issued for this application are accepted.
     *
     * @param clientId the Google OAuth client id used as the expected token audience
     */
    public GoogleTokenServiceImpl(@Value("${authorization.google.client-id}") String clientId) {
        this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    /**
     * Validate a Google id_token and extract its verified claims.
     *
     * @param idToken the Google id_token to be validated
     * @return a {@link GoogleUserInfo} containing the verified claims of the Google account
     * @throws IllegalArgumentException if the token is invalid, expired, has an unexpected
     *         audience or the email is not verified
     */
    @Override
    public GoogleUserInfo verify(String idToken) {
        GoogleIdToken googleIdToken;
        try {
            googleIdToken = verifier.verify(idToken);
        } catch (GeneralSecurityException | IOException e) {
            LOGGER.error("Error verifying Google id_token: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid Google id_token");
        }

        if (googleIdToken == null) {
            throw new IllegalArgumentException("Invalid Google id_token");
        }

        var payload = googleIdToken.getPayload();
        if (!Boolean.TRUE.equals(payload.getEmailVerified())) {
            throw new IllegalArgumentException("Google account email is not verified");
        }

        return new GoogleUserInfo(
                payload.getSubject(),
                payload.getEmail(),
                (String) payload.get(GIVEN_NAME_CLAIM),
                (String) payload.get(FAMILY_NAME_CLAIM));
    }
}
