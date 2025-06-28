package dev.dcarminatti.rja_api.service;

import dev.dcarminatti.rja_api.model.entity.Comment;
import dev.dcarminatti.rja_api.model.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    // Basic CRUD operations
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    public Comment save(Comment comment) {
        if (comment.getDateTime() == null) {
            comment.setDateTime(LocalDateTime.now());
        }
        return commentRepository.save(comment);
    }

    public Comment update(Long id, Comment updatedComment) {
        return commentRepository.findById(id)
                .map(comment -> {
                    comment.setText(updatedComment.getText());
                    comment.setDateTime(updatedComment.getDateTime());
                    comment.setAuthor(updatedComment.getAuthor());
                    comment.setTicket(updatedComment.getTicket());
                    return commentRepository.save(comment);
                })
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
    }

    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    // Business logic methods
    public List<Comment> findByTicket(Long ticketId) {
        return commentRepository.findByTicketId(ticketId);
    }

    public List<Comment> findByAuthor(Long authorId) {
        return commentRepository.findByAuthorId(authorId);
    }

    public List<Comment> findByTicketOrderedAsc(Long ticketId) {
        return commentRepository.findByTicketIdOrderByDateTimeAsc(ticketId);
    }

    public List<Comment> findByTicketOrderedDesc(Long ticketId) {
        return commentRepository.findByTicketIdOrderByDateTimeDesc(ticketId);
    }

    public List<Comment> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return commentRepository.findByDateTimeBetween(startDate, endDate);
    }

    public List<Comment> findByTextContaining(String text) {
        return commentRepository.findByTextContaining(text);
    }

    public Long countByTicket(Long ticketId) {
        return commentRepository.countByTicketId(ticketId);
    }

    public Long countByAuthor(Long authorId) {
        return commentRepository.countByAuthorId(authorId);
    }

    public Comment addCommentToTicket(Long ticketId, String text, Long authorId) {
        Comment comment = new Comment();
        comment.setText(text);
        comment.setDateTime(LocalDateTime.now());
        // Here you would typically fetch the User and Ticket entities
        // For now, we'll assume they are set elsewhere
        return commentRepository.save(comment);
    }
}
