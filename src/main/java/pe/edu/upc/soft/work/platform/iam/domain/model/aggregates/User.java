package pe.edu.upc.soft.work.platform.iam.domain.model.aggregates;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.CreateUserCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.UpdateUserCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;


/**
 * User aggregate root entity.
 */
@Entity
@Table(name = "users")
public class User extends AuditableAbstractAggregateRoot<User> {

    @Getter
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Getter
    @Column(name = "last_name", nullable = false, length = 100)
    @JsonProperty("lastName")
    private String lastName;

    @Getter
    @Column(name = "phone_number", nullable = false, length = 15)
    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @Getter
    @Column(name = "dni", nullable = false, length = 8)
    private String dni;

    /**
     * Default constructor for JPA.
     */
    public User(){}

    /**
     * Constructor to create a User from a CreateUserCommand.
     * @param command the command containing user details
     */
    public User(CreateUserCommand command){
        this.name = command.name();
        this.lastName = command.lastName();
        this.dni = command.dni();
        this.phoneNumber = command.phoneNumber();
    }

    /**
     * Updates the User with details from an UpdateUserCommand.
     * @param command the command containing updated user details
     */
    public void updateUser(UpdateUserCommand command)
    {
        this.name = command.name();
        this.lastName = command.lastName();
        this.dni = command.dni();
        this.phoneNumber = command.phoneNumber();
    }
}
