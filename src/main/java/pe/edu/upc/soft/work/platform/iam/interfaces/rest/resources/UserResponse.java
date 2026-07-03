package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;


/**
 * Response object representing a user in the system.
 * @param userId the unique identifier of the user
 * @param name the first name of the user
 * @param lastName the last name of the user
 * @param phoneNumber the phone number of the user
 * @param dni the national identity card number of the user
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserResponse (
        Long userId,
        String name,
        String lastName,
        String phoneNumber,
        String dni
) {
}
