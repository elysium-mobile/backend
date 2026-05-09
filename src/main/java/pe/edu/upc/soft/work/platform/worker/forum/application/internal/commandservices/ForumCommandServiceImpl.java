package pe.edu.upc.soft.work.platform.worker.forum.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Forum;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.CreateForumCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.UpdateForumCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands.DeleteForumCommand;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.ForumCommandService;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.ForumRepository;

import java.util.Optional;

@Service
public class ForumCommandServiceImpl implements ForumCommandService {
    private final ForumRepository forumRepository;

    public ForumCommandServiceImpl(ForumRepository forumRepository) {
        this.forumRepository = forumRepository;
    }

    @Override
    public Long handle(CreateForumCommand command) {
        var forum = new Forum(command);
        try {
            forumRepository.save(forum);
        } catch (Exception e) {
            throw new RuntimeException("Error creating Forum: " + e.getMessage(), e);
        }
        return forum.getId();
    }

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
