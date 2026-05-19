package pe.edu.upc.soft.work.platform.iam.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.CreateRRHHProfileCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.DeleteRRHHProfileCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.UpdateRRHHProfileCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.entities.RRHHProfile;
import pe.edu.upc.soft.work.platform.iam.domain.services.RRHHProfileCommandService;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.RRHHProfileRepository;

import java.util.Optional;

@Service
public class RRHHProfileCommandServiceImpl implements RRHHProfileCommandService {

    private final RRHHProfileRepository rrhhProfileRepository;

    public RRHHProfileCommandServiceImpl(RRHHProfileRepository rrhhProfileRepository) {
        this.rrhhProfileRepository = rrhhProfileRepository;
    }

    @Override
    public Long handle(CreateRRHHProfileCommand command) {
        var rrhhProfile = new RRHHProfile(command);
        try {
            rrhhProfileRepository.save(rrhhProfile);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving RRHH profile: %s".formatted(e.getMessage()));
        }
        return rrhhProfile.getId();
    }

    @Override
    public Optional<RRHHProfile> handle(UpdateRRHHProfileCommand command) {
        var rrhhId = command.RRHHProfileId();
        var rrhhToUpdate = rrhhProfileRepository.findById(rrhhId).get();
        rrhhToUpdate.updateRRHHProfile(command);
        try {
            var updatedRRHHProfile = rrhhProfileRepository.save(rrhhToUpdate);
            return Optional.of(updatedRRHHProfile);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while updating RRHH profile: %s".formatted(e.getMessage()));
        }
    }

    @Override
    public void handle(DeleteRRHHProfileCommand command) {
        if (!rrhhProfileRepository.existsById(command.rrhhProfileId())) {
            throw new IllegalArgumentException("RRHH profile with id %s not found".formatted(command.rrhhProfileId()));
        }
        try {
            rrhhProfileRepository.deleteById(command.rrhhProfileId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting RRHH profile: %s".formatted(e.getMessage()));
        }

    }
}
