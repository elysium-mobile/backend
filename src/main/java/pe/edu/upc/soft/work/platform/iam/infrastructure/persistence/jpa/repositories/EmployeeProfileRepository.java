package pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.iam.domain.model.entities.EmployeeProfile;

/**
 * This interface defines the repository for managing EmployeeProfile entities in the database.
 */
@Repository
public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {
}
