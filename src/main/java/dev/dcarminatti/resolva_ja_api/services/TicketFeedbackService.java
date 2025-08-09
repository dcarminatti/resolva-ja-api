package dev.dcarminatti.resolva_ja_api.services;

import dev.dcarminatti.resolva_ja_api.exceptions.ValidateException;
import dev.dcarminatti.resolva_ja_api.models.entities.TicketFeedback;
import dev.dcarminatti.resolva_ja_api.models.repositories.TicketFeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketFeedbackService {
    @Autowired
    private TicketFeedbackRepository repository;

    public List<TicketFeedback> findAll() {
        return repository.findAll();
    }

    public Optional<TicketFeedback> findById(Long id) {
        return repository.findById(id);
    }

    public TicketFeedback save(TicketFeedback entity) throws ValidateException {
        entity.setId(null);
        entity.setDate(LocalDateTime.now().toString());
        this.validateTicketComment(entity);
        return repository.save(entity);
    }

    public TicketFeedback update(TicketFeedback entity) throws ValidateException {
        this.validateTicketComment(entity);
        return repository.save(entity);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public TicketFeedback findByTicketId(Long id) {
        return repository.findByTicketId(id);
    }

    private void validateTicketComment(TicketFeedback entity) {
        if (entity.getComment() == null || entity.getComment().isEmpty()) {
            throw new ValidateException("Comment cannot be empty");
        }
        if (entity.getRating() < 1 || entity.getRating() > 5) {
            throw new ValidateException("Rating must be between 1 and 5");
        }
    }

}

