package pe.edu.upc.soft.work.platform.iam.domain.model.commands;

import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;

/**
 * Command to create a new RRHH profile.
 * @param RRHHDepartment the department of the RRHH profile
 * @param statusHierarchy the status hierarchy of the RRHH profile
 * @param userAccountId the user account associated with the RRHH profile
 */
public record CreateRRHHProfileCommand(String RRHHDepartment, String statusHierarchy, Long userAccountId) {

    /**
     * Constructor with validations.
     * @param RRHHDepartment the department of the RRHH profile
     * @param statusHierarchy the status hierarchy of the RRHH profile
     * @param userAccountId the user account associated with the RRHH profile
     */
     public CreateRRHHProfileCommand {
         if (RRHHDepartment == null || RRHHDepartment.isBlank()) {
             throw new IllegalArgumentException("[CreateRRHHProfileCommand] RRHH department must not be null or blank");
         }
         if (statusHierarchy == null || statusHierarchy.isBlank()) {
             throw new IllegalArgumentException("[CreateRRHHProfileCommand] status hierarchy must not be null or blank");
         }
         if (userAccountId == null) {
             throw new IllegalArgumentException("[CreateRRHHProfileCommand] user account id must not be null");
         }
     }
}
