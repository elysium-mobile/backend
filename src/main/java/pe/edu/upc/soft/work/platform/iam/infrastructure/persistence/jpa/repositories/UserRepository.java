package pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.User;

/**
 * Repository interface for managing User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    /**
     * Checks if a user with the given DNI exists in the database.
     * @param dni The DNI of the user to check for existence.
     * @return true if a user with the given DNI exists, false otherwise.
     */
    boolean existsByDni(String dni);
}
