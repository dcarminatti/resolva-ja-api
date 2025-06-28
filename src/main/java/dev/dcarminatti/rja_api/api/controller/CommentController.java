package dev.dcarminatti.rja_api.api.controller;

import dev.dcarminatti.rja_api.exception.ResourceNotFoundException;
import dev.dcarminatti.rja_api.model.entity.Comment;
import dev.dcarminatti.rja_api.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    public ResponseEntity<List<Comment>> getAllComments() {
        List<Comment> comments = commentService.findAll();
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        Comment comment = commentService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));
        return ResponseEntity.ok(comment);
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(@Valid @RequestBody Comment comment) {
        Comment savedComment = commentService.save(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @Valid @RequestBody Comment comment) {
        Comment updatedComment = commentService.update(id, comment);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        if (!commentService.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Comment", "id", id);
        }
        commentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Business logic endpoints
    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<Comment>> getCommentsByTicket(@PathVariable Long ticketId) {
        List<Comment> comments = commentService.findByTicket(ticketId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<Comment>> getCommentsByAuthor(@PathVariable Long authorId) {
        List<Comment> comments = commentService.findByAuthor(authorId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/ticket/{ticketId}/ordered-asc")
    public ResponseEntity<List<Comment>> getCommentsByTicketOrderedAsc(@PathVariable Long ticketId) {
        List<Comment> comments = commentService.findByTicketOrderedAsc(ticketId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/ticket/{ticketId}/ordered-desc")
    public ResponseEntity<List<Comment>> getCommentsByTicketOrderedDesc(@PathVariable Long ticketId) {
        List<Comment> comments = commentService.findByTicketOrderedDesc(ticketId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Comment>> getCommentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Comment> comments = commentService.findByDateRange(startDate, endDate);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Comment>> searchCommentsByText(@RequestParam String text) {
        List<Comment> comments = commentService.findByTextContaining(text);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/count/ticket/{ticketId}")
    public ResponseEntity<Long> countCommentsByTicket(@PathVariable Long ticketId) {
        Long count = commentService.countByTicket(ticketId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/author/{authorId}")
    public ResponseEntity<Long> countCommentsByAuthor(@PathVariable Long authorId) {
        Long count = commentService.countByAuthor(authorId);
        return ResponseEntity.ok(count);
    }

    @PostMapping("/ticket/{ticketId}/add")
    public ResponseEntity<Comment> addCommentToTicket(@PathVariable Long ticketId, @RequestParam String text, @RequestParam Long authorId) {
        Comment comment = commentService.addCommentToTicket(ticketId, text, authorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }
}
