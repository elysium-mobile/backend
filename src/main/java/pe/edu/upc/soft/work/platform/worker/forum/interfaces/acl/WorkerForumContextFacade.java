package pe.edu.upc.soft.work.platform.worker.forum.interfaces.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateForumCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.entities.Category;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetForumByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.CompanyId;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.ForumCommandService;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.ForumQueryService;

import java.util.List;
import java.util.Objects;

/**
 * Facade for the Worker Forum Bounded Context.
 * Exposes forum verification and creation operations for other Bounded Contexts.
 */
@Service
public class WorkerForumContextFacade {

    /**
     * Query service for forums.
     */
    private final ForumQueryService forumQueryService;

    /**
     * Command service for forums.
     */
    private final ForumCommandService forumCommandService;

    /**
     * Constructor for WorkerForumContextFacade.
     *
     * @param forumQueryService   the forum query service
     * @param forumCommandService the forum command service
     */
    public WorkerForumContextFacade(ForumQueryService forumQueryService,
                                    ForumCommandService forumCommandService) {
        this.forumQueryService = forumQueryService;
        this.forumCommandService = forumCommandService;
    }

    /**
     * Check if a forum exists by its ID.
     *
     * @param forumId the ID of the forum
     * @return true if the forum exists, false otherwise
     */
    public boolean existsForumById(Long forumId) {
        var query = new GetForumByIdQuery(forumId);
        return this.forumQueryService.handle(query).isPresent();
    }

    /**
     * Create a new forum associated to a company.
     *
     * @param title       the title of the forum
     * @param description the description of the forum
     * @param companyId   the ID of the company owning the forum
     * @return the ID of the created forum, or 0L if creation failed
     */
    public Long createForum(String title, String description, Long companyId, List<Category> categories) {
        var command = new CreateForumCommand(title, description, new CompanyId(companyId), categories);
        var forumId = this.forumCommandService.handle(command);
        if (Objects.isNull(forumId)) {
            return 0L;
        }
        return forumId;
    }
}
