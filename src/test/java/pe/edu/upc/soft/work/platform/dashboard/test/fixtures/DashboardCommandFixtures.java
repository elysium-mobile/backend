package pe.edu.upc.soft.work.platform.dashboard.test.fixtures;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateAreaCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateDashboardCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateUnitOfWorkCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateWidgetCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateWorkTeamCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateAreaCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateDashboardCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateUnitOfWorkCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateWidgetCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateWorkTeamCommand;

/**
 * Dashboard-specific command factories. Mirrors the architectural
 * template established by {@code IamCommandFixtures}: every static
 * method returns a fresh, valid command instance built from canonical
 * constants. Tests MUST NOT instantiate dashboard commands inline.
 */
public final class DashboardCommandFixtures {

    public static final String VALID_COMPANY_NAME = "Acme Corp";
    public static final String VALID_RUC = "20123456789";
    public static final String VALID_CONTACT_EMAIL = "contact@acme.pe";
    public static final String VALID_CONTACT_PHONE = "987654321";

    public static final String VALID_AREA_NAME = "Engineering";
    public static final Integer VALID_ANNUAL_BUDGET = 100_000;

    public static final String VALID_UNIT_OF_WORK_NAME = "Backend Platform";

    public static final String VALID_WIDGET_TITLE = "Throughput";
    public static final Integer VALID_REFRESH_PERIOD = 30;

    public static final String VALID_TEAM_NAME = "Avengers";
    public static final String VALID_TEAM_LEADER = "Steve Rogers";
    public static final Long VALID_COMPANY_ID = 0L;
    public static final String VALID_TITLE = "Title";
    public static final String VALID_DESCRIPTION = "Description";
    public static final Long VALID_DASHBOARD_ID = 1L;
    public static final Long VALID_UNIT_OF_WORK_ID = 2L;


    private DashboardCommandFixtures() {
        throw new AssertionError("DashboardCommandFixtures is a utility class and must not be instantiated.");
    }

    // ---------- Company ----------
    public static CreateCompanyCommand validCreateCompanyCommand() {
        return new CreateCompanyCommand(
                VALID_COMPANY_NAME, VALID_RUC, VALID_CONTACT_EMAIL, VALID_CONTACT_PHONE,null,null);
    }

    public static UpdateCompanyCommand updateCompanyCommand(Long companyId) {
        return new UpdateCompanyCommand(
                companyId, VALID_COMPANY_NAME, VALID_RUC, VALID_CONTACT_EMAIL, VALID_CONTACT_PHONE);
    }

    // ---------- Dashboard ----------
    public static CreateDashboardCommand validCreateDashboardCommand() {
        return new CreateDashboardCommand(VALID_RUC, null, null, null, null);
    }

    public static UpdateDashboardCommand updateDashboardCommand(Long dashboardId) {
        return new UpdateDashboardCommand(dashboardId,null, null,VALID_RUC,null);
    }

    // ---------- AreaCompany ----------
    public static CreateAreaCompanyCommand validCreateAreaCompanyCommand() {
        return new CreateAreaCompanyCommand(VALID_AREA_NAME, VALID_ANNUAL_BUDGET, null, null);
    }

    public static UpdateAreaCompanyCommand updateAreaCompanyCommand(Long areaCompanyId) {
        return new UpdateAreaCompanyCommand(areaCompanyId, VALID_AREA_NAME, VALID_ANNUAL_BUDGET,null);
    }

    // ---------- UnitOfWork ----------
    public static CreateUnitOfWorkCommand validCreateUnitOfWorkCommand() {
        return new CreateUnitOfWorkCommand(VALID_UNIT_OF_WORK_NAME, null);
    }

    public static UpdateUnitOfWorkCommand updateUnitOfWorkCommand(Long unitOfWorkId) {
        return new UpdateUnitOfWorkCommand(unitOfWorkId, VALID_UNIT_OF_WORK_NAME);
    }

    // ---------- Widget ----------
    public static CreateWidgetCommand validCreateWidgetCommand() {
        return new CreateWidgetCommand(VALID_WIDGET_TITLE, VALID_REFRESH_PERIOD,null);
    }

    public static UpdateWidgetCommand updateWidgetCommand(Long widgetId) {
        return new UpdateWidgetCommand(widgetId, VALID_WIDGET_TITLE, VALID_REFRESH_PERIOD,null);
    }

    // ---------- WorkTeam ----------
    public static CreateWorkTeamCommand validCreateWorkTeamCommand() {
        return new CreateWorkTeamCommand(VALID_TEAM_NAME, VALID_TEAM_LEADER,null);
    }

    public static UpdateWorkTeamCommand updateWorkTeamCommand(Long workTeamId) {
        return new UpdateWorkTeamCommand(workTeamId, VALID_TEAM_NAME, VALID_TEAM_LEADER,null);
    }
}
