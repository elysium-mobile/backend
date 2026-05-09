package pe.edu.upc.soft.work.platform.iam.domain.services;

import pe.edu.upc.soft.work.platform.iam.domain.model.entities.EmployeeProfile;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetAllEmployeeProfileQuery;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetEmployeeProfileByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for handling queries related to employee profiles. This interface defines methods for retrieving employee profile information based on various criteria, such as employee ID, department, or role. Implementations of this interface will provide the actual logic for fetching and processing employee profile data from the underlying data source.
 */
public interface EmployeeProfileQueryService {

    /**
     * Retrieves a list of all employee profiles in the system.
     * @return a list of employee profiles
     */
    List<EmployeeProfile> handle(GetAllEmployeeProfileQuery query);

    /**
     * Retrieves an employee profile based on the provided employee ID.
     * @param query the query containing the employee ID for which to retrieve the profile
     * @return an Optional containing the employee profile if found, or an empty Optional if no profile exists for the given employee ID
     */
    Optional<EmployeeProfile> handle(GetEmployeeProfileByIdQuery query);

}
