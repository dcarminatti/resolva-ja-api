package dev.dcarminatti.rja_api.model.repository;

import dev.dcarminatti.rja_api.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    List<Comment> findByTicketId(Long ticketId);
    
    List<Comment> findByAuthorId(Long authorId);
    
    List<Comment> findByTicketIdOrderByDateTimeAsc(Long ticketId);
    
    List<Comment> findByTicketIdOrderByDateTimeDesc(Long ticketId);
    
    List<Comment> findByDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT c FROM Comment c WHERE c.text LIKE %:text%")
    List<Comment> findByTextContaining(@Param("text") String text);
    
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.ticket.id = :ticketId")
    Long countByTicketId(@Param("ticketId") Long ticketId);
    
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.author.id = :authorId")
    Long countByAuthorId(@Param("authorId") Long authorId);
}
