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
}
