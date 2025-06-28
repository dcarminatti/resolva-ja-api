package dev.dcarminatti.rja_api.model.repository;

import dev.dcarminatti.rja_api.model.entity.Ticket;
import dev.dcarminatti.rja_api.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    
    List<Ticket> findByStatus(Status status);
    
    List<Ticket> findByRequesterId(Long requesterId);
    
    List<Ticket> findByLocationId(Long locationId);
    
    List<Ticket> findByAssociatedSubcategoryId(Long subcategoryId);
    
    @Query("SELECT t FROM Ticket t JOIN t.associatedTechnicians tech WHERE tech.id = :technicianId")
    List<Ticket> findByAssociatedTechnicianId(@Param("technicianId") Long technicianId);
    
    @Query("SELECT t FROM Ticket t WHERE t.title LIKE %:title%")
    List<Ticket> findByTitleContaining(@Param("title") String title);
    
    @Query("SELECT t FROM Ticket t WHERE t.description LIKE %:description%")
    List<Ticket> findByDescriptionContaining(@Param("description") String description);
    
    List<Ticket> findByOpeningDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Ticket> findByClosingDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT t FROM Ticket t WHERE t.status = :status AND t.openingDateTime < :dateTime")
    List<Ticket> findOverdueTickets(@Param("status") Status status, @Param("dateTime") LocalDateTime dateTime);
    
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.requester.id = :requesterId")
    Long countByRequesterId(@Param("requesterId") Long requesterId);
    
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.status = :status")
    Long countByStatus(@Param("status") Status status);
}
