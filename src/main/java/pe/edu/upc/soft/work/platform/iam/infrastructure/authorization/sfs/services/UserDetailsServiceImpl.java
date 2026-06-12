package pe.edu.upc.soft.work.platform.iam.infrastructure.authorization.sfs.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.UserAccountRepository;

/**
 * This class is responsible for providing the user details to the Spring Security framework.
 * It implements the UserDetailsService interface.
 *
 * <p>UserAccount uses {@code email} as the login identifier, so
 * {@code loadUserByUsername} receives the email and looks it up accordingly.</p>
 */
@Service(value = "defaultUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    /**
     * Constructor with dependencies.
     *
     * @param userAccountRepository The user account repository.
     */
    public UserDetailsServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    /**
     * Loads the user account by email (used as username in this project).
     *
     * @param email The email address acting as the login identifier.
     * @return The UserDetails object.
     * @throws UsernameNotFoundException If the user account is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userAccountRepository.findByEmail(email)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User account not found with email: " + email));
        return UserDetailsImpl.build(user);
    }
}
