package pe.edu.upc.soft.work.platform.iam.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.iam.domain.model.entities.EmployeeProfile;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetAllEmployeeProfileQuery;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetEmployeeProfileByIdQuery;
import pe.edu.upc.soft.work.platform.iam.domain.services.EmployeeProfileQueryService;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.EmployeeProfileRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the EmployeeProfileQueryService interface.
 */
@Service
public class EmployeeProfileQueryServiceImpl implements EmployeeProfileQueryService {

    private final EmployeeProfileRepository employeeProfileRepository;

    /**
     * Constructor of the class.
     * @param employeeProfileRepository the repository to be used by the class.
     */
    public EmployeeProfileQueryServiceImpl(EmployeeProfileRepository employeeProfileRepository) {
        this.employeeProfileRepository = employeeProfileRepository;
    }

    /**
     * Handle the GetAllEmployeeProfileQuery query.
     * @param query the query to be handled.
     * @return the list of employee profiles.
     */
    @Override
    public List<EmployeeProfile> handle(GetAllEmployeeProfileQuery query) {
        return employeeProfileRepository.findAll();
    }

    /**
     * Handle the GetEmployeeProfileByIdQuery query.
     * @param query the query containing the employee ID for which to retrieve the profile
     * @return an Optional containing the EmployeeProfile if found, or empty if not found
     */
    @Override
    public Optional<EmployeeProfile> handle(GetEmployeeProfileByIdQuery query) {
        return employeeProfileRepository.findById(query.employeeId());
    }
}
