package pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.soft.work.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateReportCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateReportCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.AreaCompanyId;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.UserAccountId;

import java.util.Date;

@Entity
@Table(name = "reports")
public class Report extends AuditableAbstractAggregateRoot<Report> {

    @Getter
    @Column(name = "reason", nullable = false)
    private String reason;

    @Getter
    @Column(name = "description", nullable = false)
    private String description;

    @Getter
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "userAccountId", column = @Column(name = "user_account_id", nullable = false))
    })
    @JsonProperty("id_user_account")
    private UserAccountId userAccountId;

    @Getter
    @Column(name = "report_date", nullable = false)
    private Date reportDate;

    @Getter
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "areaCompanyId", column = @Column(name = "area_company_id", nullable = false))
    })
    private AreaCompanyId areaCompanyId;

    public Report() {
    }

    public Report(CreateReportCommand command){
        this.reason = command.reason();
        this.description = command.description();
        this.userAccountId = command.userAccountId();
        this.reportDate = command.reportDate();
        this.areaCompanyId = command.areaCompanyId();
    }

    public void updateReport(UpdateReportCommand command){
        this.reason = command.reason();
        this.description = command.description();
        this.userAccountId = command.userAccountId();
        this.reportDate = command.reportDate();
        this.areaCompanyId = command.areaCompanyId();
    }


}
