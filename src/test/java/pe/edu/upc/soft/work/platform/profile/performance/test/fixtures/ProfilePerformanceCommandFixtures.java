package pe.edu.upc.soft.work.platform.profile.performance.test.fixtures;

import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.CreateCommentEmployeeCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.CreatePerformanceCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.UpdateCommentEmployeeCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands.UpdatePerformanceCommand;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.valueobjects.EmployeeProfileId;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.valueobjects.RRHHProfileId;

import java.util.Date;

/**
 * Profile Performance-specific command factories. Mirrors the architectural
 * template established by sibling fixture utilities. Tests MUST NOT
 * instantiate profile-performance commands inline.
 */
public final class ProfilePerformanceCommandFixtures {

    public static final String VALID_COMMENT_TITLE = "Quarterly Review";
    public static final String VALID_COMMENT_CONTENT = "Excellent performance this quarter.";
    public static final Long VALID_RRHH_PROFILE_ID = 11L;

    public static final Long VALID_EMPLOYEE_PROFILE_ID = 22L;
    public static final Date VALID_PERFORMANCE_DATE = new Date(1_700_000_000_000L);
    public static final Integer VALID_CLASSIFICATION = 5;

    public static final Long VALID_PERFORMANCE_ID = 67L;


    private ProfilePerformanceCommandFixtures() {
        throw new AssertionError("ProfilePerformanceCommandFixtures is a utility class and must not be instantiated.");
    }

    // ---------- CommentEmployee ----------
    public static CreateCommentEmployeeCommand validCreateCommentEmployeeCommand() {
        return new CreateCommentEmployeeCommand(
                VALID_COMMENT_TITLE, VALID_COMMENT_CONTENT, new RRHHProfileId(VALID_RRHH_PROFILE_ID),
            VALID_PERFORMANCE_ID);
    }

    public static UpdateCommentEmployeeCommand updateCommentEmployeeCommand(Long commentEmployeeId) {
        return new UpdateCommentEmployeeCommand(
                commentEmployeeId, VALID_COMMENT_TITLE, VALID_COMMENT_CONTENT, new RRHHProfileId(VALID_RRHH_PROFILE_ID),VALID_PERFORMANCE_ID);
    }

    // ---------- Performance ----------
    public static CreatePerformanceCommand validCreatePerformanceCommand() {
        return new CreatePerformanceCommand(
                new EmployeeProfileId(VALID_EMPLOYEE_PROFILE_ID), VALID_PERFORMANCE_DATE, VALID_CLASSIFICATION,null);
    }

    public static UpdatePerformanceCommand updatePerformanceCommand(Long performanceId) {
        return new UpdatePerformanceCommand(
                performanceId, new EmployeeProfileId(VALID_EMPLOYEE_PROFILE_ID), VALID_PERFORMANCE_DATE, VALID_CLASSIFICATION);
    }
}
