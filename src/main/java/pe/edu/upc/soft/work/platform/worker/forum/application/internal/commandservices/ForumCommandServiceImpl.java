package pe.edu.upc.soft.work.platform.worker.forum.application.internal.commandservices;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;
import pe.edu.upc.soft.work.platform.worker.forum.application.internal.outboundservices.acl.ExternalDashboardServiceFromWorkerForum;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Forum;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateForumCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateForumCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteForumCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.events.ForumCreatedEvent;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.ForumCommandService;
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
    /**
     * Constructor for ForumCommandServiceImpl.
     * @param forumRepository the repository for Forum persistence
     */
    public ForumCommandServiceImpl(ForumRepository forumRepository,
                                   ExternalDashboardServiceFromWorkerForum externalDashboardServiceFromWorkerForum,
                                   ApplicationEventPublisher eventPublisher) {
        this.forumRepository = forumRepository;
        this.externalDashboardServiceFromWorkerForum = externalDashboardServiceFromWorkerForum;
        this.eventPublisher = eventPublisher;
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
        eventPublisher.publishEvent(new ForumCreatedEvent(this, forum.getId(), forum.getCompanyId(), forum.getTitle()));
        try {
            forumRepository.save(forum);
        } catch (Exception e) {
            throw new RuntimeException("Error creating Forum: " + e.getMessage(), e);
        }
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
            throw new RuntimeException("Forum with ID " + forumId + " does not exist.");
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
     * Handles the deletion of an Forum
     * @param command the command to delete a Forum
     */
    @Override
    public void handle(DeleteForumCommand command) {
        if (!forumRepository.existsById(command.forumId())) {
            throw new RuntimeException("Forum with ID " + command.forumId() + " does not exist.");
        }
        try {
            forumRepository.deleteById(command.forumId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting Forum: " + e.getMessage(), e);
        }
    }
}
