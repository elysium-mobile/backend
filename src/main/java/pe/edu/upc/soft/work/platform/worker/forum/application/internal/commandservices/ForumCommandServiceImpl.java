package pe.edu.upc.soft.work.platform.worker.forum.application.internal.commandservices;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;
import pe.edu.upc.soft.work.platform.worker.forum.application.internal.outboundservices.acl.ExternalDashboardServiceFromWorkerForum;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Forum;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.AddCategoryToForumCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateForumCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateForumCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteForumCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.events.ForumCreatedEvent;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.ForumCommandService;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.CategoryRepository;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.ForumRepository;

import java.util.Optional;

/**
 * Service implementation for handling Forum commands
 */
@Service
public class ForumCommandServiceImpl implements ForumCommandService {
    private final ForumRepository forumRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ExternalDashboardServiceFromWorkerForum externalDashboardServiceFromWorkerForum;
    private final CategoryRepository categoryRepository;
    /**
     * Constructor for ForumCommandServiceImpl.
     * @param forumRepository the repository for Forum persistence
     */
    public ForumCommandServiceImpl(ForumRepository forumRepository,
                                   ExternalDashboardServiceFromWorkerForum externalDashboardServiceFromWorkerForum,
                                   ApplicationEventPublisher eventPublisher,
                                   CategoryRepository categoryRepository) {
        this.forumRepository = forumRepository;
        this.externalDashboardServiceFromWorkerForum = externalDashboardServiceFromWorkerForum;
        this.eventPublisher = eventPublisher;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Handles the creation of a Forum
     * @param command the command to create a Forum
     * @return the generated ID of the new Forum
     */
    @Override
    public Long handle(CreateForumCommand command) {
        if (!this.externalDashboardServiceFromWorkerForum.existsCompanyById(command.companyId().companyId())){
            throw new NotFoundArgumentException(String.format("[ForumCommandServiceImpl] Company ID: %s not found in the external Dashboard service",
                    command.companyId().companyId()));
        }

        var forum = new Forum(command);
        try {
            forumRepository.save(forum);
        } catch (Exception e) {
            throw new RuntimeException("Error creating Forum: " + e.getMessage(), e);
        }
        eventPublisher.publishEvent(new ForumCreatedEvent(this, forum.getId(), forum.getCompanyId(), forum.getTitle()));
        return forum.getId();
    }

    /**
     * Handles the update of an existing Forum
     * @param command the command to update an Forum
     * @return the updated Forum as an Optional
     */
    @Override
    public Optional<Forum> handle(UpdateForumCommand command) {
        var forumId = command.forumId();
        if (!this.forumRepository.existsById(forumId)) {
            throw new NotFoundArgumentException(
                    String.format("Forum with ID %s does not exist.", forumId));
        }

        var forumToUpdate = this.forumRepository.findById(forumId).get();
        forumToUpdate.updateForum(command);
        try {
            var updatedForum = this.forumRepository.save(forumToUpdate);
            return Optional.of(updatedForum);
        } catch (Exception e) {
            throw new RuntimeException("Error updating Forum: " + e.getMessage(), e);
        }
    }

    /**
     * Handles the deletion of an existing Forum
     * @param command the command to delete a Forum
     */
    @Override
    public void handle(DeleteForumCommand command) {
        if (!forumRepository.existsById(command.forumId())) {
            throw new NotFoundArgumentException(
                    String.format("Forum with ID %s does not exist.", command.forumId()));
        }
        try {
            forumRepository.deleteById(command.forumId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting Forum: " + e.getMessage(), e);
        }
    }

    @Override
    public void handle(AddCategoryToForumCommand command) {
        var category = categoryRepository.findById(command.categoryId()).orElseThrow(() -> new NotFoundArgumentException(
                String.format("[ForumCommandServiceImpl] Category ID: %s not found in the database",
                        command.categoryId())));

        var forum = forumRepository.findById(command.forumId()).orElseThrow(() -> new NotFoundArgumentException(
                String.format("[ForumCommandServiceImpl] Forum ID: %s not found in the database",
                        command.forumId())));
        try {
            forum.addCategory(category);
            forumRepository.save(forum);
        } catch (Exception e) {
            throw new RuntimeException("Error adding Category to Forum: " + e.getMessage(), e);
        }
    }
}
