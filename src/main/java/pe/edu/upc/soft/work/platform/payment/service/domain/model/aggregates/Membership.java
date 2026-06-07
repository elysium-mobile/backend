package pe.edu.upc.soft.work.platform.payment.service.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.CreateMembershipCommand;
import pe.edu.upc.soft.work.platform.payment.service.domain.model.commands.UpdateMembershipCommand;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import java.util.Date;
import java.util.List;

import pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects.MembershipStatus;

/**
 * Membership aggregate root entity.
 */
@Entity
@Table(name="memberships")
public class Membership extends AuditableAbstractAggregateRoot<Membership> {

    @Getter
    @Column(name = "membership_start", nullable = false)
    private Date membershipStart;
    @Getter
    @Column(name = "membership_over", nullable = false)
    private Date membershipOver;
    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "membership_status", nullable = false, length = 20)
    private MembershipStatus membershipStatus;

    /**
     * Default constructor for JPA.
     */
    public Membership() {}

    /**
     * Constructor to create a Membership from a CreateMembershipCommand.
     * @param command the command containing membership details
     */
    public Membership(CreateMembershipCommand command) {
        this.membershipStart = command.membershipStart();
        this.membershipOver = command.membershipOver();
        this.membershipStatus = command.membershipStatus();
    }

    /**
     * Updates the Membership with details from an UpdateMembershipCommand.
     * @param command the command containing updated membership details
     */
    public void updateMembership(UpdateMembershipCommand command) {
        this.membershipStart = command.membershipStart();
        this.membershipOver = command.membershipOver();
        this.membershipStatus = command.membershipStatus();
    }
}
