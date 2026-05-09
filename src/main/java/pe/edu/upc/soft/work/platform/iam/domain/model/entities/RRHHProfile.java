package pe.edu.upc.soft.work.platform.iam.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.CreateRRHHProfileCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.UpdateRRHHProfileCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Entity
@Table(name = "rrhh_profiles")
public class RRHHProfile extends AuditableAbstractAggregateRoot<RRHHProfile> {

    @Getter
    @Column(name = "rrhh_department", nullable = false, length = 100)
    private String RRHHDepartment;


    @Getter
    @Column(name = "status_hierarchy", nullable = false, length = 100)
    private String statusHierarchy;

    @Getter
    @JoinColumn(name = "user_account_id", nullable = false)
    private Long userAccountId;

    /**
     * Default constructor for JPA.
     */
    public RRHHProfile(){}

    /**
     * Constructor to create an RRHHProfile from a CreateRRHHProfileCommand.
     * @param command the command containing RRHH profile details
     */
    public RRHHProfile(CreateRRHHProfileCommand command)
    {
        this.RRHHDepartment = command.RRHHDepartment();
        this.statusHierarchy = command.statusHierarchy();
        this.userAccountId = command.userAccountId();
    }

    /**
     * Method to update the RRHH profile using an UpdateRRHHProfileCommand.
     * @param command the command containing updated RRHH profile details
     */
    public void updateRRHHProfile(UpdateRRHHProfileCommand command) {
        this.RRHHDepartment = command.RRHHDepartment();
        this.statusHierarchy = command.statusHierarchy();
    }
}
