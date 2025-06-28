package dev.dcarminatti.rja_api.service;

import dev.dcarminatti.rja_api.model.entity.Ticket;
import dev.dcarminatti.rja_api.model.enums.Status;
import dev.dcarminatti.rja_api.model.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    // Basic CRUD operations
    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> findById(Long id) {
        return ticketRepository.findById(id);
    }

    public Ticket save(Ticket ticket) {
        if (ticket.getOpeningDateTime() == null) {
            ticket.setOpeningDateTime(LocalDateTime.now());
        }
        if (ticket.getStatus() == null) {
            ticket.setStatus(Status.OPEN);
        }
        return ticketRepository.save(ticket);
    }

    public Ticket update(Long id, Ticket updatedTicket) {
        return ticketRepository.findById(id)
                .map(ticket -> {
                    ticket.setTitle(updatedTicket.getTitle());
                    ticket.setDescription(updatedTicket.getDescription());
                    ticket.setLocation(updatedTicket.getLocation());
                    ticket.setAssociatedSubcategory(updatedTicket.getAssociatedSubcategory());
                    ticket.setStatus(updatedTicket.getStatus());
                    ticket.setRequester(updatedTicket.getRequester());
                    ticket.setAssociatedTechnicians(updatedTicket.getAssociatedTechnicians());
                    ticket.setClosingDateTime(updatedTicket.getClosingDateTime());
                    return ticketRepository.save(ticket);
                })
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));
    }

    public void deleteById(Long id) {
        ticketRepository.deleteById(id);
    }

    // Business logic methods
    public List<Ticket> findByStatus(Status status) {
        return ticketRepository.findByStatus(status);
    }

    public List<Ticket> findByRequester(Long requesterId) {
        return ticketRepository.findByRequesterId(requesterId);
    }

    public List<Ticket> findByLocation(Long locationId) {
        return ticketRepository.findByLocationId(locationId);
    }

    public List<Ticket> findBySubcategory(Long subcategoryId) {
        return ticketRepository.findByAssociatedSubcategoryId(subcategoryId);
    }

    public List<Ticket> findByTechnician(Long technicianId) {
        return ticketRepository.findByAssociatedTechnicianId(technicianId);
    }

    public List<Ticket> findByTitleContaining(String title) {
        return ticketRepository.findByTitleContaining(title);
    }

    public List<Ticket> findByDescriptionContaining(String description) {
        return ticketRepository.findByDescriptionContaining(description);
    }

    public List<Ticket> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return ticketRepository.findByOpeningDateTimeBetween(startDate, endDate);
    }

    public List<Ticket> findOverdueTickets() {
        return ticketRepository.findOverdueTickets(Status.OPEN, LocalDateTime.now().minusHours(24));
    }

    public Ticket closeTicket(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .map(ticket -> {
                    ticket.setStatus(Status.CLOSED);
                    ticket.setClosingDateTime(LocalDateTime.now());
                    return ticketRepository.save(ticket);
                })
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + ticketId));
    }

    public Ticket updateStatus(Long ticketId, Status status) {
        return ticketRepository.findById(ticketId)
                .map(ticket -> {
                    ticket.setStatus(status);
                    if (status == Status.CLOSED && ticket.getClosingDateTime() == null) {
                        ticket.setClosingDateTime(LocalDateTime.now());
                    }
                    return ticketRepository.save(ticket);
                })
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + ticketId));
    }

    public Long countByRequester(Long requesterId) {
        return ticketRepository.countByRequesterId(requesterId);
    }

    public Long countByStatus(Status status) {
        return ticketRepository.countByStatus(status);
    }

    public List<Ticket> findOpenTickets() {
        return ticketRepository.findByStatus(Status.OPEN);
    }

    public List<Ticket> findInProgressTickets() {
        return ticketRepository.findByStatus(Status.IN_PROGRESS);
    }

    public List<Ticket> findClosedTickets() {
        return ticketRepository.findByStatus(Status.CLOSED);
    }
}
