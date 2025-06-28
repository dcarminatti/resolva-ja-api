package dev.dcarminatti.rja_api.model.repository;

import dev.dcarminatti.rja_api.model.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    
    List<Attachment> findByTicketId(Long ticketId);
    
    List<Attachment> findByUploaderId(Long uploaderId);
    
    Optional<Attachment> findByFileName(String fileName);
    
    List<Attachment> findByFileNameContaining(String fileName);
    
    @Query("SELECT a FROM Attachment a WHERE a.fileUrl LIKE %:url%")
    List<Attachment> findByFileUrlContaining(@Param("url") String url);
    
    @Query("SELECT COUNT(a) FROM Attachment a WHERE a.ticket.id = :ticketId")
    Long countByTicketId(@Param("ticketId") Long ticketId);
    
    @Query("SELECT COUNT(a) FROM Attachment a WHERE a.uploader.id = :uploaderId")
    Long countByUploaderId(@Param("uploaderId") Long uploaderId);
    
    boolean existsByFileName(String fileName);
}
