package pe.edu.upc.soft.work.platform.iam.domain.model.aggregates;


import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.CreateUserAccountCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.UpdateUserAccountCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Entity
@Table(name = "user_accounts")
public class UserAccount extends AuditableAbstractAggregateRoot<UserAccount> {

    @Getter
    @JoinColumn(name = "user_id")
    private Long userId;

    @Getter
    @Column(name = "email", unique = true)
    private String email;

    @Getter
    @Column(name = "password", nullable = false)
    private String password;

    @Getter
    @Column(name = "anonymous_name", nullable = false)
    private String anonymousName;

    /**
     * Default constructor for JPA.
     */
    public UserAccount() {}

    /**
     * Constructor to create a UserAccount from a CreateUserAccountCommand.
     * @param command the command containing user account details
     */
    public UserAccount(CreateUserAccountCommand command)
    {
        this.userId = command.userId();
        this.email = command.email();
        this.password = command.password();
        this.anonymousName = command.anonymousName();
    }

    /**
     * Updates the UserAccount with details from an UpdateUserAccountCommand.
     * @param command the command containing updated user account details
     */
    public void UpdateUserAccount(UpdateUserAccountCommand command)
    {
        this.email = command.email();
        this.password = command.password();
        this.anonymousName = command.anonymousName();
    }


}
