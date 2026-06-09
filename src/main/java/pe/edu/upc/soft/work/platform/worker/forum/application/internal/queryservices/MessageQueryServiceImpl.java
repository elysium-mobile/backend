package pe.edu.upc.soft.work.platform.worker.forum.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.aggregates.Message;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetMessageByIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetAllMessageQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetMessageByUserAccountIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.queries.GetMessagesByThreadIdQuery;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.MessageQueryService;
import pe.edu.upc.soft.work.platform.worker.forum.infrastructure.persistence.jpa.repositories.MessageRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the MessageQueryService interface.
 */
@Service
public class MessageQueryServiceImpl implements MessageQueryService {
    private final MessageRepository messageRepository;

    /**
     * Constructor for MessageQueryServiceImpl.
     */
    public MessageQueryServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * Handles the GetAllMessageQuery.
     */
    @Override
    public List<Message> handle(GetAllMessageQuery query) {
        return messageRepository.findAll();
    }

    /**
     * Handles the GetMessageByIdQuery.
     */
    @Override
    public Optional<Message> handle(GetMessageByIdQuery query) {
        return messageRepository.findById(query.messageId());
    }

    @Override
    public List<Message> handle(GetMessageByUserAccountIdQuery query) {
        return messageRepository.findByUserAccountId(query.userAccountId());
    }

    @Override
    public List<Message> handle(GetMessagesByThreadIdQuery query) {
        return messageRepository.findByThreadId(query.threadId());
    }
}
