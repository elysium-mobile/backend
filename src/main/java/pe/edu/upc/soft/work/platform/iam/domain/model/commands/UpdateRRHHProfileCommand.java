package pe.edu.upc.soft.work.platform.iam.domain.model.commands;

/**
 * Command to update an existing RRHH profile.
 * @param RRHHProfileId the identifier of the RRHH profile to be updated
 * @param RRHHDepartment the department of the RRHH profile
 * @param statusHierarchy the status hierarchy of the RRHH profile
 */
public record UpdateRRHHProfileCommand(Long RRHHProfileId, String RRHHDepartment, String statusHierarchy) {

    /**
     * Constructor with validations.
     * @param RRHHProfileId the identifier of the RRHH profile to be updated
     * @param RRHHDepartment the department of the RRHH profile
     * @param statusHierarchy the status hierarchy of the RRHH profile
     */
    public UpdateRRHHProfileCommand {
        if (RRHHProfileId == null) {
            throw new IllegalArgumentException("[UpdateRRHHProfileCommand] RRHH profile id must not be null");
        }
        if (RRHHDepartment == null || RRHHDepartment.isBlank()) {
            throw new IllegalArgumentException("[UpdateRRHHProfileCommand] RRHH department must not be null or blank");
        }
        if (statusHierarchy == null || statusHierarchy.isBlank()) {
            throw new IllegalArgumentException("[UpdateRRHHProfileCommand] status hierarchy must not be null or blank");
        }
    }
}
