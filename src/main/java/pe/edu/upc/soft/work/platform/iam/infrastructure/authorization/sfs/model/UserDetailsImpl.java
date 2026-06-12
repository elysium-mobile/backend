package pe.edu.upc.soft.work.platform.iam.infrastructure.authorization.sfs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;

import java.util.Collection;
import java.util.List;

/**
 * This class is responsible for providing the user details to the Spring Security framework.
 * It implements the UserDetails interface.
 *
 * <p>UserAccount uses {@code email} as the unique login identifier (username).
 * Role management is not yet modelled on UserAccount, so authorities are left empty
 * and can be extended later.</p>
 */
@Getter
@EqualsAndHashCode
public class UserDetailsImpl implements UserDetails {

    private final String username;
    @JsonIgnore
    private final String password;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;
    private final Collection<? extends GrantedAuthority> authorities;

    /**
     * Constructor for UserDetailsImpl.
     *
     * @param username    The username (email) of the user.
     * @param password    The hashed password of the user.
     * @param authorities The granted authorities of the user.
     */
    public UserDetailsImpl(String username, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }

    /**
     * Builds a UserDetailsImpl from a UserAccount.
     *
     * <p>The {@code email} field of UserAccount is used as the Spring Security
     * username because UserAccount does not have a separate username field.
     * Authorities are empty; extend this method when roles are added to UserAccount.</p>
     *
     * @param userAccount The user account entity.
     * @return The UserDetailsImpl object.
     */
    public static UserDetailsImpl build(UserAccount userAccount) {
        return new UserDetailsImpl(
                userAccount.getEmail(),
                userAccount.getPassword(),
                List.of()
        );
    }
}
