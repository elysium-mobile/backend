package pe.edu.upc.soft.work.platform.iam.domain.model.commands;

/**
 * Command to delete a RRHH profile.
 * @param rrhhProfileId the identifier of the RRHH profile to be deleted
 */
public record DeleteRRHHProfileCommand(Long rrhhProfileId) {
}
