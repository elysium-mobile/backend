package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response object representing a user in the system.
 * @param userId the unique identifier of the user
 * @param name the first name of the user
 * @param lastName the last name of the user
 * @param phoneNumber the phone number of the user
 * @param dni the national identity card number of the user
 */
public record UserResponse (
        Long userId,
        @JsonProperty("name")
        String name,
        @JsonProperty("last_name")
        String lastName,
        @JsonProperty("phone_number")
        String phoneNumber,
        @JsonProperty("dni")
        String dni
) {
}
