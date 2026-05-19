package pe.edu.upc.soft.work.platform.iam.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.CreateEmployeeProfileCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.DeleteEmployeeProfileCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.UpdateEmployeeProfileCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.entities.EmployeeProfile;
import pe.edu.upc.soft.work.platform.iam.domain.services.EmployeeProfileCommandService;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.EmployeeProfileRepository;

import java.util.Optional;

@Service
public class EmployeeProfileCommandServiceImpl implements EmployeeProfileCommandService {

    private final EmployeeProfileRepository employeeProfileRepository;

    public EmployeeProfileCommandServiceImpl(EmployeeProfileRepository employeeProfileRepository) {
        this.employeeProfileRepository = employeeProfileRepository;
    }

    @Override
    public Long handle(CreateEmployeeProfileCommand command) {
        var employeeProfile = new EmployeeProfile(command);
        try {
            employeeProfileRepository.save(employeeProfile);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving employee profile: %s".formatted(e.getMessage()));
        }
        return employeeProfile.getId();
    }

    @Override
    public Optional<EmployeeProfile> handle(UpdateEmployeeProfileCommand command) {
        var employeeId = command.employeeProfileId();
        var employeeToUpdate = this.employeeProfileRepository.findById(employeeId).get();
        employeeToUpdate.updateEmployeeProfile(command);
        try {
            var updatedEmployeeProfile = employeeProfileRepository.save(employeeToUpdate);
            return Optional.of(updatedEmployeeProfile);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while updating employee profile: %s".formatted(e.getMessage()));
        }
    }

    @Override
    public void handle(DeleteEmployeeProfileCommand command) {
        if (!employeeProfileRepository.existsById(command.employeeProfileId()))
            throw new IllegalArgumentException("Employee profile with id %s not found".formatted(command.employeeProfileId()));
        try {
            employeeProfileRepository.deleteById(command.employeeProfileId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting employee profile: %s".formatted(e.getMessage()));
        }
    }
}
