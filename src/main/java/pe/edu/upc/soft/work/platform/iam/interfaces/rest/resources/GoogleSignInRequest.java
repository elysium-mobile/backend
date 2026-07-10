package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

/**
 * Request resource carrying the Google id_token to be validated during Google sign-in.
 * @param idToken the Google id_token issued by Google Identity Services (mapped from {@code id_token})
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GoogleSignInRequest(String idToken) {
}
