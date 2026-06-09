package pe.edu.upc.soft.work.platform.worker.forum.test.fixtures;

import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateAssetCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateCategoryCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateForumCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateMessageCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateThreadCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateAssetCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateCategoryCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateForumCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateMessageCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateThreadCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.AreaCompanyId;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.CompanyId;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.FileType;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.UserAccountId;

import java.util.Date;

/**
 * Worker Forum-specific command factories. Mirrors the architectural
 * template established by sibling fixture utilities. Tests MUST NOT
 * instantiate worker-forum commands inline.
 */
public final class WorkerForumCommandFixtures {

    public static final Long VALID_MESSAGE_ID = 7L;
    public static final String VALID_ATTACHMENT_NAME = "report.pdf";
    public static final String VALID_ATTACHMENT_URL = "https://files.example/report.pdf";
    public static final String VALID_FILE_SIZE = "1024KB";
    public static final FileType VALID_FILE_TYPE = FileType.PDF;
    public static boolean VALID_FILE_TYPE_IS_VIEWABLE = true;
    public static boolean VALID_FILE_TYPE_IS_READABLE = false;

    public static final Long VALID_CATEGORY_ID = 3L;
    public static final String VALID_CATEGORY_TITLE = "General Discussion";
    public static final String VALID_CATEGORY_DESCRIPTION = "Open conversation among workers";

    public static final Long VALID_FORUM_ID = 6L;
    public static final String VALID_FORUM_TITLE = "Engineering Forum";
    public static final String VALID_FORUM_DESCRIPTION = "Internal engineering discussion";
    public static final Long VALID_COMPANY_ID = 5L;

    public static final Long VALID_USER_ACCOUNT_ID = 10L;
    public static final String VALID_MESSAGE_CONTENT = "Hello team!";

    public static final Long VALID_THREAD_ID = 11L;
    public static final String VALID_THREAD_TITLE = "Release planning";
    public static final Long VALID_AREA_COMPANY_ID = 3L;
    public static final Date VALID_LAST_MESSAGE = new Date(1_700_000_000_000L);

    public static final Integer VALID_COUNT_MESSAGES = 0;
    private WorkerForumCommandFixtures() {
        throw new AssertionError("WorkerForumCommandFixtures is a utility class and must not be instantiated.");
    }

    // ---------- Attachment ----------
    public static CreateAssetCommand validCreateAssetCommand() {
        return new CreateAssetCommand(
                VALID_MESSAGE_ID, VALID_ATTACHMENT_NAME, VALID_ATTACHMENT_URL, VALID_FILE_SIZE, VALID_FILE_TYPE);
    }

    public static UpdateAssetCommand updateAssetCommand(Long attachmentId) {
        return new UpdateAssetCommand(
                attachmentId, VALID_MESSAGE_ID, VALID_ATTACHMENT_NAME, VALID_ATTACHMENT_URL, VALID_FILE_SIZE);
    }

    // ---------- Category ----------
    public static CreateCategoryCommand validCreateCategoryCommand() {
        return new CreateCategoryCommand(VALID_CATEGORY_TITLE, VALID_CATEGORY_DESCRIPTION,VALID_FORUM_ID, null);
    }

    public static UpdateCategoryCommand updateCategoryCommand(Long categoryId) {
        return new UpdateCategoryCommand(categoryId, VALID_CATEGORY_TITLE, VALID_CATEGORY_DESCRIPTION,VALID_FORUM_ID);
    }

    // ---------- Forum ----------
    public static CreateForumCommand validCreateForumCommand() {
        return new CreateForumCommand(VALID_FORUM_TITLE, VALID_FORUM_DESCRIPTION, new CompanyId(VALID_COMPANY_ID),null);
    }

    public static UpdateForumCommand updateForumCommand(Long forumId) {
        return new UpdateForumCommand(forumId, VALID_FORUM_TITLE, VALID_FORUM_DESCRIPTION, new CompanyId(VALID_COMPANY_ID));
    }

    // ---------- Message ----------
    public static CreateMessageCommand validCreateMessageCommand() {
        return new CreateMessageCommand(new UserAccountId(VALID_USER_ACCOUNT_ID), VALID_MESSAGE_CONTENT, VALID_THREAD_ID, null);
    }

    public static UpdateMessageCommand updateMessageCommand(Long messageId) {
        return new UpdateMessageCommand(messageId, new UserAccountId(VALID_USER_ACCOUNT_ID), VALID_MESSAGE_CONTENT, VALID_THREAD_ID);
    }

    // ---------- Thread ----------
    public static CreateThreadCommand validCreateThreadCommand() {
        return new CreateThreadCommand(VALID_THREAD_TITLE, new AreaCompanyId(VALID_AREA_COMPANY_ID), VALID_LAST_MESSAGE, VALID_CATEGORY_ID,VALID_COUNT_MESSAGES, null);
    }

    public static UpdateThreadCommand updateThreadCommand(Long threadId) {
        return new UpdateThreadCommand(threadId, VALID_THREAD_TITLE, new AreaCompanyId(VALID_AREA_COMPANY_ID), VALID_LAST_MESSAGE,VALID_CATEGORY_ID,VALID_COUNT_MESSAGES);
    }
}
