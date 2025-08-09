package dev.dcarminatti.resolva_ja_api.services;

import dev.dcarminatti.resolva_ja_api.exceptions.ValidateException;
import dev.dcarminatti.resolva_ja_api.models.entities.TicketComment;
import dev.dcarminatti.resolva_ja_api.models.repositories.TicketCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketCommentService {
    @Autowired
    private TicketCommentRepository ticketCommentRepository;

    public List<TicketComment> findAll() {
        return ticketCommentRepository.findAll();
    }

    public TicketComment findById(Long id) {
        return ticketCommentRepository.findById(id).orElse(null);
    }

    public TicketComment save(TicketComment comment) {
        try {
            comment.setId(null);
            this.validateComment(comment);
            return ticketCommentRepository.save(comment);
        } catch (ValidateException e) {
            throw new ValidateException("Error saving comment: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while saving the comment", e);
        }
    }

    public void update(Long id, TicketComment comment) {
        try {
            comment.setId(id);
            this.validateComment(comment);
            ticketCommentRepository.save(comment);
        } catch (ValidateException e) {
            throw new ValidateException("Error updating comment: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while updating the comment", e);
        }
    }

    public void deleteById(Long id) {
        ticketCommentRepository.deleteById(id);
    }

    public List<TicketComment> findByTicketId(Long id) {
        return ticketCommentRepository.findByTicketId(id);
    }

    private void validateComment(TicketComment comment) {
        if (comment.getComment() == null || comment.getComment().isEmpty()) {
            throw new ValidateException("Comment content cannot be empty");
        }
        if (comment.getTicket() == null) {
            throw new ValidateException("Comment must be associated with a ticket");
        }
        if (comment.getUser() == null) {
            throw new ValidateException("Comment must be associated with a user");
        }
    }

}
