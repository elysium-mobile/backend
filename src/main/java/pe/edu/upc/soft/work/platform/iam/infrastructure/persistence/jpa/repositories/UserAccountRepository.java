package pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    /**
     * Find a user account by email.
     * Since UserAccount uses email as login identifier (no separate username field),
     * this method is used by UserDetailsServiceImpl.loadUserByUsername(email).
     *
     * @param email the email address used as login identifier
     * @return Optional containing the UserAccount if found
     */
    Optional<UserAccount> findByEmail(String email);

    /**
     * Check if a user account exists by its email.
     *
     * @param email the email of the user account
     * @return true if the user account exists, false otherwise
     */
    boolean existsByEmail(String email);

}
