package pe.edu.upc.soft.work.platform.iam.domain.model.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.CreateEmployeeProfileCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.UpdateEmployeeProfileCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.valueobjects.WorkOfTeamId;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.util.Date;

@Entity
@Table(name = "employee_profiles")
public class EmployeeProfile extends AuditableAbstractAggregateRoot<EmployeeProfile> {

    @Getter
    @Column(name = "date_start", nullable = false)
    private Date dateStart;

    @Getter
    @Column(name="position", nullable = false, length = 100)
    private String position;

    @Getter
    @Column(name = "salary", nullable = false)
    private Integer salary;

    @Getter
    @Embedded
    @AttributeOverride(
            name = "id",
            column = @Column(name = "work_of_team_id", nullable = false)
    )
    @JsonProperty("work_of_team_id")
    private WorkOfTeamId workOfTeamId;

    @Getter
    @Column(name = "user_account_id", nullable = false)
    private Long userAccountId;


    public EmployeeProfile(){}

    /**
     * Constructor to create an EmployeeProfile from a CreateEmployeeProfileCommand.
     * @param command the command containing employee profile details
     */
    public EmployeeProfile(CreateEmployeeProfileCommand command)
    {
        this.dateStart = command.dateStart();
        this.position = command.position();
        this.salary = command.salary();
        this.userAccountId = command.userAccountId();
        this.workOfTeamId = command.workOfTeamId();
    }

    /**
     * Method to update the EmployeeProfile with new details from a CreateEmployeeProfileCommand.
     * @param command the command containing updated employee profile details
     */
    public void updateEmployeeProfile(UpdateEmployeeProfileCommand command) {
        this.dateStart = command.dateStart();
        this.position = command.position();
        this.salary = command.salary();
    }
}
